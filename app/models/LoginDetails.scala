package models

import play.api.data.Form
import play.api.data.Forms._

import scala.collection.mutable.ListBuffer

case class LoginDetails(username: String, password: String)

object LoginDetails {

  var userList: ListBuffer[LoginDetails] = ListBuffer(LoginDetails("admin", "password"))

  val loginForm: Form[LoginDetails] = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginDetails.apply)(LoginDetails.unapply)
  )

  def checkIfUserIsValid(userDetails: LoginDetails): Boolean = userList.contains(userDetails)

  def getUsername(username: String): Option[LoginDetails] = userList.find(user => user.username == username)


}
