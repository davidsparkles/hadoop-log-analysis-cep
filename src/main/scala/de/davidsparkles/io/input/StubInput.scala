package de.davidsparkles.io.input

import de.davidsparkles.core.io.Input


class StubInput[Type](private val entries: Array[Type]) extends Input[Type] {

  override def receive: Iterator[Type] = entries.toIterator
}
