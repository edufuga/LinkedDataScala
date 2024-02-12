package com.edufuga.scala.streaming

import cats.effect.{IO, IOApp}
import com.edufuga.scala.streaming.Model._
import com.edufuga.scala.streaming.Data._
import fs2.io.file.{Files, Path}
import fs2.{Pipe, Pure, Stream, text}

import java.net.URL

object Model {
  case class Actor(id: Int, firstName: String, lastName: String)
}

object Data {
  // Justice League
  val henryCavil: Actor = Actor(0, "Henry", "Cavill")
  val galGodot: Actor = Actor(1, "Gal", "Godot")
  val ezraMiller: Actor = Actor(2, "Ezra", "Miller")
  val benFisher: Actor = Actor(3, "Ben", "Fisher")
  val rayHardy: Actor = Actor(4, "Ray", "Hardy")
  val jasonMomoa: Actor = Actor(5, "Jason", "Momoa")

  // Avengers
  val scarlettJohansson: Actor = Actor(6, "Scarlett", "Johansson")
  val robertDowneyJr: Actor = Actor(7, "Robert", "Downey Jr.")
  val chrisEvans: Actor = Actor(8, "Chris", "Evans")
  val markRuffalo: Actor = Actor(9, "Mark", "Ruffalo")
  val chrisHemsworth: Actor = Actor(10, "Chris", "Hemsworth")
  val jeremyRenner: Actor = Actor(11, "Jeremy", "Renner")
  val tomHolland: Actor = Actor(13, "Tom", "Holland")
  val tobeyMaguire: Actor = Actor(14, "Tobey", "Maguire")
  val andrewGarfield: Actor = Actor(15, "Andrew", "Garfield")
}

object Main extends IOApp.Simple {
  val jlActors: Stream[Pure, Actor] = Stream(
    henryCavil,
    galGodot,
    ezraMiller,
    benFisher,
    rayHardy,
    jasonMomoa
  )

  val actors: Stream[IO, Actor] = jlActors.covary

  val unchangedActors: Stream[IO, Actor] = actors.flatMap { actor =>
    Stream.eval(
      IO {
        println(actor)
        actor
      }
    )
  }

  // evalMap = flatMap + Stream.eval
  val moreUnchangedActors: Stream[IO, Actor] = actors.evalMap { actor  =>
    IO {
      actor
    }
  }

  val actorNames: Stream[IO, String] = moreUnchangedActors.map(_.firstName)

  val actorNamesPrinted: Stream[IO, String] = actorNames.evalTap(IO.println)

  val drained: IO[Unit] = actorNamesPrinted.compile.drain

  val readFrom: String = "products.csv"

  val readFromJavaResource: URL = getClass.getClassLoader.getResource(readFrom)
  val readFromJavaPath: java.nio.file.Path = java.nio.file.Paths.get(readFromJavaResource.toURI).toAbsolutePath

  val readFromPath: Path = Path.fromNioPath(readFromJavaPath)

  val source: Stream[IO, Byte] = Files[IO].readAll(readFromPath)

  def lineParser[F[_]]: Pipe[F, Byte, String] =
    _.through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      //.map(_.split(',').toList) // Too simplistic. It doesn't work with our "ugly data".

  val sourceThroughParser: Stream[IO, String] = source.through(lineParser)

  val stuff: Stream[IO, String] = sourceThroughParser.evalTap(IO.println)

  /* Now comes the good stuff. */

  val productsLine = "I241-8776317,Strain Compensator,12,68,15,8,Baldwin.Dirksen@company.org,\"0,50 EUR\""

  val servicesLine = "Y704-9764759,Product Analysis,\"O491-3823912, I965-1821441, Z655-3173353, U733-5722614, K411-1729714\",Lambert.Faust@company.org,\"748,40 EUR\""

  object Patterns {
    val start = "^".r
    val end = "$".r

    // TODO: Separate it further into auxiliary subcategories (for the IDs, etc.).
    val digit = "[0-9]"
    val character = "[A-Za-z]"
    val characterOrDigit = "[A-Za-z0-9]"
    val number = s"$digit+".r
    val word = s"$character+"
    val numericalWord = s"$characterOrDigit+".r

    // TODO: Include uppercase letter at the beginning of each word?
    val words = s"(?:$word+)(?:\\s(?:$word))*".r           // possibly several words, separated by whitespaces.
    val numericalWords = s"(?:$numericalWord+)(?:\\s(?:$numericalWord))*".r

    // Number with a fixed number of digits
    def digits(cardinality: Int) = s"$digit{$cardinality}"

    // Word with a fixed number of characters
    def characters(cardinality: Int) = s"$character{$cardinality}"

    val decimalNumber = s"$number,$number".r
    val price = s"$decimalNumber\\s$word".r
    val quotedPrice = s"\"$price\"".r

    val email = s"$numericalWords(?:\\.$numericalWords)+@$numericalWords(?:\\.$numericalWords)+".r

    val idPrefix = s"$character${digits(3)}"
    val idSuffix = s"${digits(7)}"
    val id = s"$idPrefix-$idSuffix".r
    val ids = s"(?:$id+)(?:,\\s(?:$id))*".r // list of comma separated IDs
    val quotedIds = s"\"$ids\"".r         // list of comma separated IDs within a pair of quotes

    object Product {
      val productId = id
      val productName = words
      val height = number
      val width = number
      val depth = number
      val weight = number
      val productManager = email
      val price = quotedPrice
    }

    object Service {
      val serviceId = id
      val serviceName = words
      val products = quotedIds
      val productManager = email
      val price = quotedPrice
    }

    object Products {
      val line = s"$start(${Product.productId}),(${Product.productName}),(${Product.height}),(${Product.width}),(${Product.depth}),(${Product.weight}),(${Product.productManager}),(${Product.price})$end".r
    }

    object Services {
      val line = s"$start(${Service.serviceId}),(${Service.serviceName}),(${Service.products}),(${Service.productManager}),(${Service.price})$end".r
    }
  }

  println(s"Products line: '$productsLine'")

  println()

  val Patterns.Products.line(i,n,h,w,d,wg,m,p) = productsLine
  println("Product line from CSV")
  println(i)
  println(n)
  println(h)
  println(w)
  println(d)
  println(wg)
  println(m)
  println(p)

  println()

  val Patterns.Services.line(si, sn, sp, sm, spr) = servicesLine
  println("Service line from CSV")
  println(si)
  println(sn)
  println(sp)
  println(sm)
  println(spr)

  println()

  // TODO: Create Scala objects for Product and Service (lists of entities).

  override def run: IO[Unit] = stuff.compile.drain
}