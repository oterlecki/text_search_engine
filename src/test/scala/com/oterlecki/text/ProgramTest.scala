package com.oterlecki.text

import java.io.File

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.TableDrivenPropertyChecks
import Program._
import Ranking._
import org.scalamock.scalatest._
import org.scalatest.matchers.should.Matchers._

class ProgramTest extends AnyFunSuite with TableDrivenPropertyChecks with MockFactory {

  Table(
    ("currentScore", "searchedWordsSize", "occurrenceCount", "expectedResult"),
    (0d, 5, 1, 0.2),
    (0d, 10, 1, 0.1),
    (0d, 1, 1, 1.0),
    (0d, 4, 0, 0),
    (0.2d, 5, 1, 0.4),
    (0.9d, 10, 1, 1.0),
    (0.25d, 4, 1, 0.5),
    (0.8d, 5, 0, 0.8)
  ).foreach { case (currentScore, searchedWordsSize, occurrenceCount, expectedResult) =>
    test(s"Given currentScore: $currentScore, searchedWordsSize: $searchedWordsSize, occurenceCount: $occurrenceCount" +
      s" expected result should be: $expectedResult") {
      calculateScore(currentScore, searchedWordsSize, occurrenceCount) shouldBe expectedResult
    }
  }

  Table(
    ("fileIndex", "wordsToSearch", "expectedResult"),
    (FileIndex("animals.txt", Map("dog" -> 1, "cat" -> 2)), Array("cat"), Rank("animals.txt", 1.0)),
    (FileIndex("vehicles.txt", Map("car" -> 1)), Array("motorcycle"), Rank("vehicles.txt", 0)),
    (FileIndex("fruits.txt", Map("apple" -> 11, "orange" -> 4, "pineapple" -> 1, "pear" -> 1)), Array("pear", "apple", "pineapple", "cherry"), Rank("fruits.txt", 0.75)),
    (FileIndex("vegetables.txt", Map()), Array("carrot"), Rank("vegetables.txt", 0)),
    (FileIndex("names.txt", Map("John" -> 1, "Monica" -> 2, "Paul" -> 1, "Natalie" -> 1)), Array("Natalie", "Peter"), Rank("names.txt", 0.5)),
    (FileIndex("words.txt", Map("sun" -> 1, "word" -> 2, "twenty" -> 1)), Array("sun", "twenty", "polite", "moon", "word"), Rank("words.txt", 0.6))
  ).foreach { case (fileIndex, wordsToSearch, expectedResult) =>
    test(s"Given fileIndex: $fileIndex, wordsToSearch: $wordsToSearch" +
      s" expected result should be: $expectedResult") {
      val rank: Rank = createRank(fileIndex, wordsToSearch)
      rank.copy(score = roundAt(5)(rank.score)) shouldBe expectedResult
    }
  }

  Table(
    ("index", "wordsToSearch", "expectedResult"),
    (
      Index(Seq(FileIndex("animals1.txt", Map("dog" -> 1, "cat" -> 2)), FileIndex("animals2.txt", Map("dog" -> 1)))),
      Array("dog", "cat"),
      Ranking(Seq(Rank("animals1.txt", 1), Rank("animals2.txt", 0.5)))
    ),
    (
      Index(Seq(FileIndex("fruits1.txt", Map("apple" -> 11)))),
      Array("pineapple"),
      Ranking(Seq(Rank("fruits1.txt", 0.0)))
    ),
    (
      Index(),
      Array("example", "words"),
      Ranking(Seq())
    ),
    (
      Index(Seq(FileIndex("numbers.txt", Map("1" -> 2, "4" -> 1)), FileIndex("letters.txt", Map("a" -> 1)))),
      Array("a", "1"),
      Ranking(Seq(Rank("numbers.txt", 0.5), Rank("letters.txt", 0.5)))
    ),
  ).foreach { case (index, wordsToSearch, expectedResult) =>
    test(s"Given index: $index, wordsToSearch: $wordsToSearch expected result should be: $expectedResult") {
      createRanking(index, wordsToSearch) shouldBe expectedResult
    }
  }

  test("Using should close resources") {
    val autoCloseable = mock[AutoCloseable]

    (autoCloseable.close _).expects().once()

    using(autoCloseable) { aC =>
      // do nothing
    }
  }

  test("Using should close resources when exception is thrown") {
    val autoCloseable = mock[AutoCloseable]

    (autoCloseable.close _).expects().once()

    an[RuntimeException] shouldBe thrownBy {
      using(autoCloseable) { aC =>
        throw new RuntimeException
      }
    }
  }

  def roundAt(p: Int)(n: Double): Double = {
    val s = math pow(10, p); (math round n * s) / s
  }
}