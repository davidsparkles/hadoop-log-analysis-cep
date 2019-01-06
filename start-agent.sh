nohup java -cp /opt/apps/42-22-23_hadoop_log_analysis/config:/opt/apps/42-22-23_hadoop_log_analysis/hadoop-log-analysis-cep-0.3-jar-with-dependencies.jar de.davidsparkles.AgentMain &
echo "Running under process $!"
echo "kill $!" > /opt/apps/42-22-23_hadoop_log_analysis/processid.txt
