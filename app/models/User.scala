package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.OFormat
import reactivemongo.bson.BSONObjectID

object User {
  def apply(age: Int,
            firstName: String,
            lastName: String) = new User(BSONObjectID.generate(), age, firstName, lastName)

  def unapply(user: User): Option[(BSONObjectID, Int, String, String)] = Option((user._id, user.age, user.firstName, user.lastName))

  val createUserForm: Form[User] = Form(
    mapping(
      "_id" -> ignored(BSONObjectID.generate(): BSONObjectID),
      "age" -> number,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText
    )(User.apply)(User.unapply)
  )
}

case class User(
                 val _id: BSONObjectID,
                 var age: Int,
                 var firstName: String,
                 var lastName: String)

//case class Feed(
//                 name: String,
//                 url: String)

object JsonFormats {
  import reactivemongo.play.json._
  import reactivemongo.play.json.collection.JSONCollection
  import play.api.libs.json.Json

//  implicit val feedFormat: OFormat[Feed] = Json.format[Feed]
  implicit val userFormat: OFormat[User] = Json.format[User]
  implicit val loginDetailsFormat: OFormat[LoginDetails] = Json.format[LoginDetails]
}
