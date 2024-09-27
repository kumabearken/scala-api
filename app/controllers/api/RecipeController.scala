package controllers.api

 
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Recipe,RecipeForm}
import play.api.data.FormError

import services.RecipeService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RecipeController @Inject()(cc: ControllerComponents,recipeService: RecipeService) extends AbstractController(cc) {
    // Controller methods go here
    implicit val recipeFormat = Json.format[Recipe]
    
    // def getAll = Action {
    //     val recipe = new Recipe(3,"Tomato Soup","15 min","5 people","onion, tomato, seasoning, water",450,"2016-01-12 14:10:12","2016-01-12 14:10:12")
    //     Ok(Json.toJson(recipe))
    // }
    def getAll() = Action.async { implicit request: Request[AnyContent] =>
        recipeService.listAllItems map { items =>
          Ok(Json.toJson(items))
        }
      }
     
      def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        recipeService.getItem(id) map { item =>
          Ok(Json.toJson(item))
        }
      }
     
      def add() = Action.async { implicit request: Request[AnyContent] =>
        RecipeForm.form.bindFromRequest.fold(
          // if any error in submitted data
          errorForm => {
            errorForm.errors.foreach(println)
            Future.successful(BadRequest("Error!"))
          },
          data => {
            val newRecipeItem = Recipe(0, data.title, data.making_time,data.serves,data.ingredients,data.cost,data.created_at,data.updated_at)
            recipeService.addItem(newRecipeItem).map( _ => Redirect(routes.RecipeController.getAll))
          })
      }
     
      def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        RecipeForm.form.bindFromRequest.fold(
          // if any error in submitted data
          errorForm => {
            errorForm.errors.foreach(println)
            Future.successful(BadRequest("Error!"))
          },
          data => {
            val recipeItem = Recipe(id, data.title, data.making_time,data.serves,data.ingredients,data.cost,data.created_at,data.updated_at)
            recipeService.updateItem(recipeItem).map( _ => Redirect(routes.RecipeController.getAll))
          })
      }
     
      def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        recipeService.deleteItem(id) map { res =>
          Redirect(routes.RecipeController.getAll)
        }
      }
}
