package com.oterlecki.text

object Main extends App {
  Program.readFile(args).fold(println, file => Program.iterate(Index.index(file)) )
}

