package xitrum.etag

import java.io.{File, FileOutputStream}
import org.scalatest.{FlatSpec, Matchers}

class EtagTest extends FlatSpec with Matchers {
  behavior of "ETag"

  it should "stay same while file is not modified" in {
    val file     = File.createTempFile("etag", "test")
    val filePath = file.getAbsoluteFile.toString
    file.deleteOnExit()

    val result = Etag.forFile(filePath, None, gzipped = false)

    val etag = result match {
      case Etag.Small(_, et, _, _) => et
      case _ => fail()
    }

    etag should equal (Etag.forFile(filePath, None, gzipped = false).asInstanceOf[Etag.Small].etag)

    Thread.sleep(1000)

    val stream = new FileOutputStream(file)
    stream.write(1)
    stream.close()

    etag should not equal Etag.forFile(filePath, None, gzipped = false).asInstanceOf[Etag.Small].etag
  }
}
