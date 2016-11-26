import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;
import java.net.URISyntaxException;

public class UpdateUser {

	public static class ObtainMapper extends Mapper<Object, Text, Text, Text> {

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
				return;
			}

			StringTokenizer token = new StringTokenizer(linetemp);
			String line = "";
			if(token.hasMoreTokens()) {
				line = token.nextToken();
			}
			while(token.hasMoreTokens()) {
				line = line + " " + token.nextToken();
			}
			String str[] = line.split(" ");
			if(!str[0].equals("*")) {
				Text word1 = new Text();
				Text word2 = new Text();
				word1.set(str[0]);
				word2.set(str[1] + " " + str[2] + " " + str[3] + " " + str[4] + " " + str[5]);
				context.write(word1, word2);
            }
            else {
            	if(str[2].equals("0")) {
            		Text word3 = new Text();
            		Text word4 = new Text();
            		word3.set("@");
            		word4.set(str[1] + " " + str[2]);
            		context.write(word3, word4);
            		return;
            	}
            	else {
            		Text word5 = new Text();
            		Text word6 = new Text();
            		word5.set("@");
            		word6.set(str[1] + " " + str[2]);
            		context.write(word5, word6);
	            	while(true) {
	            		FileReader reader = new FileReader("pattern");
						BufferedReader br = new BufferedReader(reader);
						String s = null;
						while((s = br.readLine()) != null) {
							String st[] = s.split(" ");
							if(st[0].equals(str[1])) {
								Text sign1 = new Text();
								Text sign2 = new Text();
								sign1.set("*");
								sign2.set(st[1] + " " + st[2] + " " + st[3] + " " + st[4] + " " + st[5]);
								context.write(sign1, sign2);
								break;
							}
						}
						br.close();
						break;
					}
            	}
            }
		}
	}

	public static class ObtainReducer extends Reducer<Text, Text, Text, Text> {
		int count = 0;
		double tag1 = 0;
		double tag2 = 0;
		double tag3 = 0;
		double tag4 = 0;
		double tag5 = 0;
		Text word1 = new Text();
		String user = "";
		String userID = "";

		public void reduce(Text key, Iterable<Text> info,
				Context context) throws IOException, InterruptedException {
			String x = key.toString();
			if(x.equals("@")) {
				for(Text v : info) {
					String y = v.toString();
					String yy[] = y.split(" ");
					word1.set(yy[0] + " " + yy[1]);
					context.write(key, word1);
				}
			}				
			else {
				if(x.equals("*")) {
					for(Text v : info) {
						String y = v.toString();
						String yy[] = y.split(" ");
						tag1 = tag1 + Double.parseDouble(yy[0]);
						tag2 = tag2 + Double.parseDouble(yy[1]);
						tag3 = tag3 + Double.parseDouble(yy[2]);
						tag4 = tag4 + Double.parseDouble(yy[3]);
						tag5 = tag5 + Double.parseDouble(yy[4]);
						count++;
						context.write(key, v);
					}
				}
				else {
					user = key.toString();
				}
			}
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			Text word2 = new Text();
			Text word3 = new Text();
			if(count == 0) {
				word2.set(user);
				word3.set("10 0 0 0 0");
				context.write(word2, word3);
			}
			else {
				tag1 = tag1 / count;
				tag2 = tag2 / count;
				tag3 = tag3 / count;
				tag4 = tag4 / count;		
				tag5 = tag5 / count;
				String a = String.valueOf(tag1);
				String b = String.valueOf(tag2);
				String c = String.valueOf(tag3);
				String d = String.valueOf(tag4);
				String e = String.valueOf(tag5);
				word2.set(user);
				word3.set(a + " " + b + " " + c + " " + d + " " + e);
				context.write(word2, word3);
			}
		}
	}

	public static class EleminateMapper extends Mapper<Object, Text, Text, Text> {
		Text word1 = new Text();
		Text word2 = new Text();

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
                return;  
            }
			StringTokenizer token = new StringTokenizer(linetemp);
			String line = "";
			if(token.hasMoreTokens())
				line = token.nextToken();
			while(token.hasMoreTokens()) {
				line = line + " " + token.nextToken();
			}

			String str[] = line.split(" ");
			if(str[0].equals("@")) {
				word1.set("*");
				word2.set(str[1] + " " + str[2]);
				context.write(word1, word2);
			}
			else {
				if(str[0].equals("*")) {
					return;
				}
				else {
					word1.set(str[0]);
					word2.set(str[1] + " " + str[2] + " " + str[3] + " " + str[4] + " " + str[5]);
					context.write(word1, word2);
				}
			}
		}
	}

	public static class EleminateReducer extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Text info,
				Context context) throws IOException, InterruptedException {
			context.write(key, info);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] other = new GenericOptionsParser(conf, args).getRemainingArgs();
		Path del1 = new Path("temp1");
		Path del2 = new Path("output");
		FileSystem hdfs = FileSystem.get(conf);
		hdfs.delete(del1, true);
		hdfs.delete(del2, true);

		DistributedCache.createSymlink(conf);
        DistributedCache.addCacheFile(new URI("song.txt"+"#pattern"), conf);

		Job job1 = new Job(conf, "Obtain");
		job1.setJarByClass(UpdateUser.class);
		job1.setMapperClass(ObtainMapper.class);
		job1.setCombinerClass(ObtainReducer.class);
		job1.setReducerClass(ObtainReducer.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job1, new Path("input"));
		FileOutputFormat.setOutputPath(job1, new Path("temp1"));
		job1.waitForCompletion(true);

		Job job2 = new Job(conf, "Eleminate");
		job2.setJarByClass(UpdateUser.class);
		job2.setMapperClass(EleminateMapper.class);
		job2.setCombinerClass(EleminateReducer.class);
		job2.setReducerClass(EleminateReducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job2, new Path("temp1"));
		FileOutputFormat.setOutputPath(job2, new Path("output"));
		job2.waitForCompletion(true);

		//hdfs.delete(del1, true);

		System.exit(1);
	}
}