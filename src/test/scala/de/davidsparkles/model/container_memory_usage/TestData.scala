package de.davidsparkles.model.container_memory_usage

import de.davidsparkles.logevents.LogEventWithParameters

object TestData {

  val logEvents: Array[LogEventWithParameters] = Array(
    new LogEventWithParameters(0, "RESOURCE_MANAGER_AUDIT", "localhost", "INFO",
      "resourcemanager.RMAuditLogger", 0, Map("operation" -> "Submit Application Request",
        "applicationId" -> "application_0000000000001_0001", "user" -> "spark")),
    new LogEventWithParameters(1,"RESOURCE_MANAGER_AUDIT", "localhost", "INFO",
      "resourcemanager.RMAuditLogger", 0, Map("operation" -> "AM Allocated Container",
        "applicationId" -> "application_0000000000001_0001", "containerId" -> "container_e02_0000000000001_0001_01_000001")),
    new LogEventWithParameters(2,"RESOURCE_MANAGER_AUDIT", "localhost", "INFO", loggingClass = "resourcemanager.RMAuditLogger", lineNumber = 0,
      parameters = Map("operation" -> "AM Allocated Container", "applicationId" -> "application_0000000000001_0001",
        "containerId" -> "container_e02_0000000000001_0001_01_000002")),
    new LogEventWithParameters(3,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 410,
      parameters = Map("containerId" -> "container_e02_0000000000001_0001_01_000001")),
    new LogEventWithParameters(4,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 410,
      parameters = Map("containerId" -> "container_e02_0000000000001_0001_01_000002")),
    new LogEventWithParameters(5,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 499,
      parameters = Map("processTree" -> "1000", "containerId" -> "container_e02_0000000000001_0001_01_000001",
        "actualPhysicalMemory" -> "16.8 MB", "configuredPhysicalMemory" -> "1 GB", "actualVirtualMemory" -> "2.2 GB",
        "configuredVirtualMemory" -> "2.1 GB")),
    new LogEventWithParameters(6,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 499,
      parameters = Map("processTree" -> "2000", "containerId" -> "container_e02_0000000000001_0001_01_000002",
        "actualPhysicalMemory" -> "20.8 MB", "configuredPhysicalMemory" -> "1 GB", "actualVirtualMemory" -> "1.9 GB",
        "configuredVirtualMemory" -> "2.1 GB")),
    new LogEventWithParameters(7,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 499,
      parameters = Map("processTree" -> "1000", "containerId" -> "container_e02_0000000000001_0001_01_000001",
        "actualPhysicalMemory" -> "205.6 MB", "configuredPhysicalMemory" -> "1 GB", "actualVirtualMemory" -> "2.3 GB",
        "configuredVirtualMemory" -> "2.1 GB")),
    new LogEventWithParameters(8,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 499,
      parameters = Map("processTree" -> "2000", "containerId" -> "container_e02_0000000000001_0001_01_000002",
        "actualPhysicalMemory" -> "204.0 MB", "configuredPhysicalMemory" -> "1 GB", "actualVirtualMemory" -> "2.3 GB",
        "configuredVirtualMemory" -> "2.1 GB")),
    new LogEventWithParameters(9,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 198,
      parameters = Map("containerId" -> "container_e02_0000000000001_0001_01_000001")),
    new LogEventWithParameters(10,"NODE_MANAGER", "localhost", "INFO", loggingClass = "monitor.ContainersMonitorImpl.run", lineNumber = 198,
      parameters = Map("containerId" -> "container_e02_0000000000001_0001_01_000002")),
    new LogEventWithParameters(11,"RESOURCE_MANAGER_AUDIT", "localhost", "INFO", loggingClass = "resourcemanager.RMAuditLogger", lineNumber = 0,
      parameters = Map("operation" -> "Application Finished - Succeeded", "applicationId" -> "application_0000000000001_0001"))
  )

  val expectedResult: ContainerMemoryUsageResult.Application = ContainerMemoryUsageResult.Application("application_0000000000001_0001",
    "spark", 0, 11, Array(
      ContainerMemoryUsageResult.Container("container_e02_0000000000001_0001_01_000001", "localhost", 3, 9, Array(
        ContainerMemoryUsageResult.MemoryObservation(5, "16.8 MB", "1 GB", "2.2 GB", "2.1 GB"),
        ContainerMemoryUsageResult.MemoryObservation(7, "205.6 MB", "1 GB", "2.3 GB", "2.1 GB")
      )),
      ContainerMemoryUsageResult.Container("container_e02_0000000000001_0001_01_000002", "localhost", 4, 10, Array(
        ContainerMemoryUsageResult.MemoryObservation(6, "20.8 MB", "1 GB", "1.9 GB", "2.1 GB"),
        ContainerMemoryUsageResult.MemoryObservation(8, "204.0 MB", "1 GB", "2.3 GB", "2.1 GB")
      ))
    )
  )
}
