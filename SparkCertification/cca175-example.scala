val data = sc.textFile("data/crime.csv")
val header = data.first()
val datafile = data.filter(rec => rec != header)
val datac1 = datafile.map(rec => {var d = rec.split(",");( d(2), d(5))})

case class datacc (
datecol: String,
ctype: String);

val datac1DF = datac1.map(r => datacc(r._1, r._2)).toDF
datac1DF.registerTempTable(“datatable”)

val Df = sqlContext.sql(“select concat(substr(datecol, 7, 4), substr(datecol, 0, 2)) month, ctype, count(1) no_of_crimes from datatable group by concat(substr(datecol, 7, 4), substr(datecol, 0, 2)), ctype order by month, no_of_crimes desc”)


Df.rdd.map(r => r(0)+"\t"+ r(1)+"\t" + r(2)).saveAsTextFile("/user/cloudera/output", classOf[org.apache.hadoop.io.compress.GzipCodec])