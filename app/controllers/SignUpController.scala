package controllers

import javax.inject.{Inject, Singleton}
import models.LoginDetails
import models.SignUpDetails
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}

@Singleton
class SignUpController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def signUp(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(SignUpDetails.signUpForm))
  }

  def signUpSubmit(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    SignUpDetails.signUpForm.bindFromRequest.fold({ formWithErrors =>
      BadRequest(views.html.signup(formWithErrors))
    }, { signUpDetails =>
      if (SignUpDetails.checkIfValidDetails(signUpDetails) && SignUpDetails.checkUserDoesNotExist(signUpDetails)) {
        SignUpDetails.addUser(signUpDetails.username, signUpDetails.confirmedPassword)
        Redirect(routes.HomeController.index()).withSession(request.session + ("username" -> signUpDetails.username))
      } else if (SignUpDetails.checkUserDoesNotExist(signUpDetails)) {
        BadRequest("Please make sure you type in the same password as before when confirming your password")
      } else {
        BadRequest("This account already exists")
      }
    })
  }

}
