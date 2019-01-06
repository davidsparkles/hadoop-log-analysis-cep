package de.davidsparkles.model

import de.davidsparkles.core.io.Output


class OutputTester[Type](test: Type => Unit) extends Output[Type] {

  override def output(entry: Type): Unit = {
    test(entry.asInstanceOf[Type])
  }
}