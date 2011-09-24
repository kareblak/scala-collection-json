package json.collection

import org.specs2.mutable.Specification
import java.io.InputStreamReader
import java.net.URI

class JsonParserSpec extends Specification {
  val parser = new JsonParser()
  val href = URI.create("http://example.org/friends/")
  "parsing json " should {
    "minimal is successful" in {
      val result = parser.parse(new InputStreamReader(classOf[JsonParserSpec].getResourceAsStream("/minimal.json")))
      result match {
        case Left(ex) => throw ex
        case Right(v) => v must beEqualTo(JsonCollection(Version.ONE, href, Nil, Nil, Nil, None, None))
      }
    }
    "minimal without version is successful" in {
      val result = parser.parse(new InputStreamReader(classOf[JsonParserSpec].getResourceAsStream("/minimal-without-version.json")))
      result match {
        case Left(ex) => throw ex
        case Right(v) => v must beEqualTo(JsonCollection(Version.ONE, href, Nil, Nil, Nil, None, None))
      }
    }

    "one item is successful" in {
      val result = parser.parse(new InputStreamReader(classOf[JsonParserSpec].getResourceAsStream("/item.json")))
      val item = Item(
        URI.create("http://example.org/friends/jdoe"),
        List(
          PropertyWithValue("full-name", Some("Full Name"), Value("J. Doe")),
          PropertyWithValue("email", Some("Email"), Value("jdoe@example.org"))
        ),
        List(
          Link(URI.create("http://examples.org/blogs/jdoe"), "blog", Some("Blog")),
          Link(URI.create("http://examples.org/images/jdoe"), "avatar", Some("Avatar"), Render.IMAGE)
        )
        )
      val links = List(
        Link(URI.create("http://example.org/friends/rss"), "feed"),
        Link(URI.create("http://example.org/friends/?queries"), "queries"),
        Link(URI.create("http://example.org/friends/?template"), "template")
      )
      result match {
        case Left(ex) => throw ex
        case Right(v) => v must beEqualTo(JsonCollection(Version.ONE, href, links, List(item), Nil, None, None))
      }

    }
  }
}
