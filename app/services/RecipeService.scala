
package services
 
import com.google.inject.Inject
import models.{Recipe, RecipeList}
 
import scala.concurrent.Future
 
class RecipeService @Inject() (items: RecipeList) {
 
  def addItem(item: Recipe): Future[String] = {
    items.add(item)
  }
 
  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }
 
  def updateItem(item: Recipe): Future[Int] = {
    items.update(item)
  }
 
  def getItem(id: Long): Future[Option[Recipe]] = {
    items.get(id)
  }
 
  def listAllItems: Future[Seq[Recipe]] = {
    items.listAll
  }
}