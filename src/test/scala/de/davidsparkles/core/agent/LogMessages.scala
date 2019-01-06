package de.davidsparkles.core.agent

object LogMessages {

  val hdfsAuditLog = "2018-05-13 09:26:08,967 INFO FSNamesystem.audit: allowed=false\tugi=spark (auth:SIMPLE)\tip=/127.0.0.1\tcmd=getfileinfo\tsrc=/user/root/data/numbers.txt\tdst=null\tperm=null\tproto=rpc\tcallerContext=SPARK_CLIENT_application_1526206706116_0007"

  val namenodeLogException = "2018-05-13 09:27:05,040 INFO  ipc.Server (Server.java:logException(2428)) - IPC Server handler 17 on 8020, call org.apache.hadoop.hdfs.protocol.ClientProtocol.getFileInfo from 127.0.0.1:34574 Call#47 Retry#0: org.apache.hadoop.security.AccessControlException: Permission denied: user=spark, access=EXECUTE, inode=\"/user/spark/data/numbers.txt/_spark_metadata\":spark:hdfs:-rw-r--r--"

  val namenodeLogBlockMgmt = "2018-05-13 09:27:05,327 INFO  BlockStateChange (UnderReplicatedBlocks.java:chooseUnderReplicatedBlocks(395)) - chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false"
}
