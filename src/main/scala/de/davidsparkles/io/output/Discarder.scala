package de.davidsparkles.io.output

import de.davidsparkles.core.io.Output


class Discarder[Type] extends Output[Type] {

  override def output(entry: Type): Unit = { }
}
