val twitterRDD = sc.textFile("twitter.edges")

val topuser = twitterRDD.flatMap(a => a.split(":")(1).trim.split(",").map(b => (b, 1))).reduceByKey((a,b) => a+b).filter(_._2 > 1000).saveAsTextFile("output")

System.exit(0)
