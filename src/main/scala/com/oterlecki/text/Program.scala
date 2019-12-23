package com.oterlecki.text

import java.io.File

import scala.annotation.tailrec
import scala.util.Try
import scala.io.StdIn.readLine
import scala.collection.immutable.Map

import com.oterlecki.text.Ranking._

object Program {
  type WordCountMap = Map[String, Int]

  sealed trait ReadFileError

  case object MissingPathArg extends ReadFileError

  case class NotDirectory(error: String) extends ReadFileError

  case class FileNotFound(t: Throwable) extends ReadFileError

  def readFile(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(s"Path [$path] is not a directory"))
        )
    } yield file
  }

  def using[A <: AutoCloseable, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  @tailrec
  def iterate(indexedFiles: Index): Unit = {
    print(s"search> ")
    val wordsToSearch = readLine()
    if (wordsToSearch == ":quit") return

    Option(createRanking(indexedFiles, wordsToSearch.split("\\s", -1)).ranks
      .sortWith(_.score > _.score)
      .take(10)
      .filter(_.score > 0.0d)
      .map(rank => f"${rank.title} : ${rank.score * 100}%2.0f%%")
    ).filter(_.nonEmpty)
      .getOrElse(Seq("No matches found."))
      .foreach(println)

    iterate(indexedFiles)
  }

}



















