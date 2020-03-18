package models

import play.api.data.Form
import play.api.data.Forms._

case class SignUpDetails(firstName: String, surname: String, username: String, password: String)

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

  def checkIfValidDetails(userDetails: SignUpDetails): Boolean = "enterPassword" == "confirmPassword"

  def addUser(userName: String, password: String) = LoginDetails.userList = LoginDetails.userList + LoginDetails(userName, password)
}
