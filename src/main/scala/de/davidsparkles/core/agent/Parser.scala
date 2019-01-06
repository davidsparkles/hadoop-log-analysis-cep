package de.davidsparkles.core.agent

trait Parser[InputType, OutputType] {

  def parse(entry: InputType): OutputType
}
