package de.davidsparkles.core.finite_state_machine

class State private (val id: Int, val name: String) {

}

object State {

  private var nextId: Int = 0

  def create(name: String = null): State = {
    val state = new State(this.nextId, if (name == null) s"State ${this.nextId}" else name)
    this.nextId += 1
    state
  }
}
