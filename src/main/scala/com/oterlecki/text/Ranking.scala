package com.oterlecki.text

import com.oterlecki.text.Program.WordCountMap

object Ranking {
  case class Ranking(ranks: Seq[Rank] = Seq.empty)

  case class Rank(title: String, score: Double)

  case class FileIndex(title: String, words: WordCountMap)

  def createRanking(index: Index, wordsToSearch: Array[String]): Ranking =
    index.indexedFiles.foldLeft(Ranking()) {
      (ranking, fileIndex) => Ranking(ranking.ranks ++ Seq(createRank(fileIndex, wordsToSearch)))
    }

  def createRank(fileIndex: FileIndex, wordsToSearch: Array[String]): Rank =
    wordsToSearch.map(word => (word, fileIndex.words.getOrElse(word, 0)))
      .foldLeft(Rank(fileIndex.title, 0.0)) {
        (rank, wordCountTuple) =>
          wordCountTuple match {
            case (_, occurrenceCount) => Rank(rank.title, calculateScore(rank.score, wordsToSearch.length, occurrenceCount))
          }
      }

  def calculateScore(currentScore: Double, searchedWordsSize: Int, occurrenceCount: Int): Double = {
    //    val occurrenceCountMultiplier: Double = min(MaximumScoredOccurrenceCount, occurrenceCount) / MaximumScoredOccurrenceCount.doubleValue()

    occurrenceCount match {
      case 0 => currentScore
      case occurrenceCount if occurrenceCount > 0 => currentScore + (1 / searchedWordsSize.doubleValue())
    }
  }}

