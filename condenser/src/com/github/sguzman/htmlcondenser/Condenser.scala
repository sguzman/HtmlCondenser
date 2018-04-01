package com.github.sguzman.htmlcondenser

/**
  * <h1>HtmlCondenser Class</h1>
  * <p>
  * This represents the main class of the entire project. The
  * user of will access any and all the functionality contained
  * within this project via any public methods accessible here.
  * </p>
  * <p>
  * This class should have mainly a single method that the user
  * will be interested in. This will be the call to the condenser's
  * subsystems. From this single interface alone, the user should
  * be able to parameterize what kind of customizable actions
  * should be taken while parsing (e.g. omit <script></script> tags,
  * omit <style></style> tags, omit comments)
  * </p>
  * <p>
  * HTML documents have the following operations performed on them
  * automatically:
  * </p>
  * <ul>
  *   <li>Remove comments</li>
  *   <li>Remove non-significant whitespace</li>
  * </ul>
  * <p>
  * The following operations are treated as optional:
  * </p>
  * <ul>
  *   <li>Remove head node</li>
  *   <li>Remove style tags</li>
  *   <li>Remove css tags</li>
  * </ul>
  *
  * <h2>Exceptional behavior</h2>
  * <p>
  * I have adopted the policy that any general exceptions thrown
  * during the execution of the logic within any method here should
  * be handled by the user. This includes such phenomena as no
  * internet connection, malformed URLs or any other error caused
  * by a combination of the parameters passed by the user. However,
  * with that said, the user can expect to handle only issues
  * concerning their code and parameters; any error caused by the
  * libraries used in this application will be handled by me.
  * </p>
  *
  * @author Salvador Guzman (guzmansalv@gmail.com)
  * @since 1.0.0
  */
object Condenser {
  /**
    * <p>
    * Given a URL object, the HTML document associated with the address,
    * will be retrieved and parsed. As with any other public method
    * declared here, comments are automatically stripped and any none
    * significant whitespace is stripped
    * </p>
    *
    * @param url       URL to be retrieved
    *
    * @param omitJS    should <script></script> tags be omitted?
    * @param omitCSS   should <style></style> tags be omitted?
    * @param omitHead  should <head></head> tag be omitted?
    *
    * @return          String consisting of condensed HTML
    *
    * @see             Condenser
    * @since           1.0.0
    */
  def condenseURL(url: URL, omitJS: Boolean = true, omitCSS: Boolean = true, omitHead: Boolean = true): String = {
    condenseString(Http(url.toString).asString.body, omitJS, omitCSS, omitHead)
  }

}
