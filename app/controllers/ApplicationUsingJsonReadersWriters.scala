package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import reactivemongo.play.json.collection.JSONCollection
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.play.json._
import collection._
import models.{User}
import models.JsonFormats._
import play.api.libs.json.{JsValue, Json}
import reactivemongo.api.Cursor

import play.modules.reactivemongo.{
  MongoController, ReactiveMongoComponents, ReactiveMongoApi
}

class ApplicationUsingJsonReadersWriters @Inject()(
                                                    components: ControllerComponents,
                                                    val reactiveMongoApi: ReactiveMongoApi
                                                  ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents {

  implicit def ec: ExecutionContext = components.executionContext

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("persons"))

  def create: Action[AnyContent] = Action.async {
    val user = User(29, "John", "Smith")
    val futureResult = collection.flatMap(_.insert.one(user))
    futureResult.map(_ => Ok("User inserted"))
  }

  def createFromJson: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[User].map { user =>
      collection.flatMap(_.insert.one(user)).map { _ => Ok("User inserted")
      }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findByName(lastName: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[User]] = collection.map {
      _.find(Json.obj("lastName" -> lastName)).
        sort(Json.obj("created" -> -1)).
        cursor[User]()
    }

    val futureUsersList: Future[List[User]] =
      cursor.flatMap(
        _.collect[List](
          -1,
          Cursor.FailOnError[List[User]]()
        )
      )

    futureUsersList.map { persons =>
      Ok(persons.toString)
    }
  }

//  def updateFirstName(firstName: String, user: User): Action[AnyContent] = Action {
//    val updatedUser = User(user.age, firstName, user.lastName, user.feeds)
//    val futureResult = collection.flatMap(_.update(user).one(updatedUser))
//    futureResult.map(_ => Ok(updatedUser))
//  }
//
//  def updateSurname(surname: String, user: User): Action[AnyContent] = Action {
//    val updatedUser = User(user.age, user.firstName, surname, user.feeds)
//    Ok(updatedUser)
//  }
//
//  def updateAge(age: Int, user: User): Action[AnyContent] = Action {
//    val updatedUser = User(age, user.firstName, user.lastName, user.feeds)
//    Ok(updatedUser)
//  }

  def deleteUser(firstName: String) = TODO



}
