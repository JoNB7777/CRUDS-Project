package models

import play.api.data.Form
import play.api.data.Forms._

import scala.collection.mutable.ListBuffer

case class SignUpDetails(firstName: String, surname: String, username: String, password: String, confirmedPassword: String)

object SignUpDetails {

  val signUpForm: Form[SignUpDetails] = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "surname" -> nonEmptyText,
      "username" -> nonEmptyText,
      "enterPassword" -> nonEmptyText,
      "confirmPassword" -> nonEmptyText
    ) (SignUpDetails.apply)(SignUpDetails.unapply)
  )

  def checkIfValidDetails(userDetails: SignUpDetails): Boolean = userDetails.password == userDetails.confirmedPassword

  def checkUserDoesNotExist(userDetails: SignUpDetails): Boolean = !(LoginDetails.userList.contains(LoginDetails(userDetails.username, userDetails.password)))

  def addUser(username: String, password: String): ListBuffer[LoginDetails] = LoginDetails.userList += LoginDetails(username, password)
}
