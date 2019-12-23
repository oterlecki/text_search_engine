package com.oterlecki.text

import java.io.File

import com.oterlecki.text.Program._
import com.oterlecki.text.Ranking._

import scala.collection.immutable.Map
import scala.io.Source

case class Index(indexedFiles: Seq[FileIndex] = Seq.empty)

object Index {
  def index(file: File): Index =
    file.listFiles.filter(_.isFile).foldLeft(Index()) {
      (idx, file) =>
        idx match {
          case Index(indexedFiles) => Index(Seq(FileIndex(file.getName, indexFile(file))) ++ indexedFiles)
        }
    }

  def indexFile(file: File): WordCountMap =
    using(Source.fromFile(file.getPath)) {
      source =>
        source.getLines.flatMap(_.split("\\s+")).foldLeft(Map(): WordCountMap) {
          (wordCountMap, word) =>
            wordCountMap match {
              case wordCountMap: WordCountMap if wordCountMap.contains(word) => wordCountMap + (word -> (1 + wordCountMap(word)))
              case wordCountMap: WordCountMap => wordCountMap + (word -> 1)
            }
        }
    }
}
