package de.davidsparkles.model.container_memory_usage


object ContainerMemoryUsageResult {

  case class Application(
                          applicationId: String,
                          user: String,
                          applicationStartTimestamp: Long,
                          applicationEndTimestamp: Long,
                          containers: Array[Container]
                        )

  case class Container(
                        containerId: String,
                        server: String,
                        containerStartTimestamp: Long,
                        containerEndTimestamp: Long,
                        memoryObservations: Array[MemoryObservation]
                      )

  case class MemoryObservation(
                                timestamp: Long,
                                actualPhysicalMemory: String,
                                configuredPhysicalMemory: String,
                                actualVirutalMemory: String,
                                configuredVirtualMemory: String
                              )
}
