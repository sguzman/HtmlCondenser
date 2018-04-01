package com.github.sguzman.htmlcondenser

import java.net.URL

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.model.Document
import org.apache.commons.lang3.StringUtils
import scalaj.http.Http

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
  *   <li>Remove empty tags</li>
  *   <li>Remove head node</li>
  * </ul>
  * <p>
  * The following operations are treated as optional:
  * </p>
  * <ul>
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
  * <p>
  *   Note that currently any HTML elements in comments will also be removed since
  *   its considered optional elements.
  * </p>
  * <p>
  * <p>
  *   Ideally, the operation `condense` which is performed here should be idempotent.
  *   If the output of the operation is passed again as input, it should match the input.
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
    *
    * @return          String consisting of condensed HTML
    *
    * @see             Condenser
    * @since           1.0.0
    */
  def condenseURL(url: URL,
                  omitJS: Boolean = true,
                  omitCSS: Boolean = true): String = {
    condenseString(Http(url.toString).asString.body, omitJS, omitCSS)
  }

  def condenseString(html: String,
                     omitJS: Boolean = true,
                     omitCSS: Boolean = true): String = {
    condenseJsoup(JsoupBrowser().parseString(html), omitJS, omitCSS)
  }

  private def stringSetOnTrue(str: String, cond: Boolean) =
    if (cond)
      Set(str)
    else
      Set.empty[String]

  private def setFromStringBools(values: Seq[(String, Boolean)]) =
    values.map(a => stringSetOnTrue(a._1, a._2)).reduce(_ ++ _)


  private def condenseJsoup(doc: Browser#DocumentType,
                                 omitJS: Boolean = true,
                                 omitCSS: Boolean = true) =
    if (doc.body.children.isEmpty) "<html><body></body></html>"
    else "<html><body>" ++ condenseExcludeNodes(doc.body,
      setFromStringBools(List(("script", omitJS), ("style", omitCSS)))
    ) ++ "</body></html>"

  private implicit final class DocWrap(element: Document#ElementType) {
    def in(set: Set[String]) = set.contains(element.tagName)
  }

  private implicit final class StrWrap(str: String) {
    def before(sep: String) = StringUtils.substringBefore(str, sep)
  }

  private def condenseExcludeNodes(doc: Document#ElementType,
                                   exclude: Set[String]): String = {
    if (doc.in(exclude))
      ""
    else if (doc.children.isEmpty)
      if (doc.innerHtml != doc.text)
        s"${doc.outerHtml.before(">")}>${doc.text}</${doc.tagName}>"
      else
        doc.outerHtml
    else
      s"${doc.outerHtml.before(">")}>${doc.children.map(a => condenseExcludeNodes(a, exclude)).reduce(_ ++ _)}</${doc.tagName}>"
  }
}
