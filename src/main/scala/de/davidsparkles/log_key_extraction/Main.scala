package de.davidsparkles.log_key_extraction

object Main extends App {
  import java.io.File

  import scala.io.Source

  val filepath = "../data/spark-yarn-perm/yarn/yarn-yarn-nodemanager-localhost.localdomain.log"

  println(new File(".").getAbsolutePath())

  val lines = Source.fromFile(filepath).getLines()
    //.filter(_.contains("ContainersMonitorImpl.java:run"))
    .map(_.split(" - ")(1))
    .map(Helper.tokenize(_))
    .toArray

  println(lines.length)
  // lines.map(_.mkString(" ")).foreach(println)

  val logKeys = LogKeyExtractor.extractLogKeys(lines)
  println(logKeys.length)
  logKeys.map(_.mkString(" ")).foreach(println)

  // val keysAndParameters = lines.map(line => LogKeyExtractor.mapToLogKeyAndParameters(line, logKeys))
  // keysAndParameters.foreach(kap => println(s"${kap._1.mkString(" ")}    ${kap._2.mkString(", ")}"))
}
