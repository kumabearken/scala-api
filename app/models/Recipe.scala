package models

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

case class Recipe(id: Long, title: String, making_time: String,serves: String,ingredients: String,cost: Long,created_at: String,updated_at: String)
case class RecipeFormData(title: String, making_time: String,serves: String,ingredients: String,cost: Long,created_at: String,updated_at: String)
 
object RecipeForm {
  val form = Form(
    mapping(
        "title" -> nonEmptyText,
        "making_time" -> nonEmptyText,
        "serves" -> nonEmptyText,
        "ingredients" -> nonEmptyText,
        "cost" -> longNumber,
        "created_at" -> nonEmptyText,
        "updated_at" -> nonEmptyText
    )(RecipeFormData.apply)(RecipeFormData.unapply)
  )
}
 
class RecipeTableDef(tag: Tag) extends Table[Recipe](tag, "recipes") {
 
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def making_time = column[String]("making_time")
    def serves = column[String]("serves")
    def ingredients = column[String]("ingredients")
    def cost = column[Long]("cost")
    def created_at = column[String]("created_at")
    def updated_at = column[String]("updated_at")
    
    override def * = (id, title, making_time, serves,ingredients,cost, created_at, updated_at) <> (Recipe.tupled, Recipe.unapply)
}
 
 
class RecipeList @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
 
  var recipeList = TableQuery[RecipeTableDef]
 
  def add(recipeItem: Recipe): Future[String] = {
    dbConfig.db
      .run(recipeList += recipeItem)
      .map(res => "RecipeItem successfully added")
      .recover {
        case ex: Exception => {
            printf(ex.getMessage())
            ex.getMessage
        }
      }
  }
 
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(recipeList.filter(_.id === id).delete)
  }
 
  def update(recipeItem: Recipe): Future[Int] = {
    dbConfig.db
      .run(recipeList.filter(_.id === recipeItem.id)
            .map(x => (x.title, x.making_time))
            .update(recipeItem.title, recipeItem.making_time)
      )
  }
 
  def get(id: Long): Future[Option[Recipe]] = {
    dbConfig.db.run(recipeList.filter(_.id === id).result.headOption)
  }
 
  def listAll: Future[Seq[Recipe]] = {
    dbConfig.db.run(recipeList.result)
  }
}