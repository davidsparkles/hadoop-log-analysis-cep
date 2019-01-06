if [ "$1" = "cep" ]
then
  CLASS=CEPMain
elif [ "$1" = "agent"  ]
then
  CLASS=AgentMain
elif [ "$1" = "runtimetest" ]
then
  CLASS=RuntimeTestMain
else
  CLASS=Test
fi

echo "Execute class $CLASS"


if [ -z "$2" ]
then
  CONFIG_DIR=.
else
  CONFIG_DIR=$2
fi

echo "Use config dir $CONFIG_DIR"

java -cp $CONFIG_DIR:target/hadoop-log-analysis-cep-0.1-SNAPSHOT-jar-with-dependencies.jar de.davidsparkles.$CLASS
