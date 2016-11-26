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

public class RecommendationTwo {

	public static class AAMapper extends Mapper<Object, Text, Text, Text> {
		Text word1 = new Text();
		Text word2 = new Text();

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
                return;  
            }
			StringTokenizer token1 = new StringTokenizer(linetemp);
			String line = "";
			if(token1.hasMoreTokens()) {
				line = token1.nextToken();
			}
			while(token1.hasMoreTokens()) {
				line = line + " " + token1.nextToken();
			}
			String str[] = line.split(" ");

			String artist = str[7] + "@";
			String album = str[8] + "*";

			FileReader reader = new FileReader("pattern");
			BufferedReader br = new BufferedReader(reader);
			String s = null;
			while((s = br.readLine()) != null) {
				StringTokenizer token2 = new StringTokenizer(s);
				String temp1 = "";
				if(token2.hasMoreTokens()) {
					temp1 = token2.nextToken();
				}
				if(temp1.contains("%")) {
					continue;
				}

				while(token2.hasMoreTokens()) {
					temp1 = temp1 + " " + token2.nextToken();
				}
				String check[] = temp1.split(" ");
				if(temp1.contains("-")) {
					if(check[1].equals(str[0])) {
						return;
					}
				}
				if(artist.equals(check[0])) {
					artist = check[1];
				}
				if(album.equals(check[0])) {
					album = check[1];
				}
			}
			word1.set(str[0]);
			word2.set(str[1] + " " + str[2] + " " + str[3] + " " + str[4] + " " + str[5] + " " + artist + " " + album);
			context.write(word1, word2);
		}
	}

	public static class AAReducer extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Text info,
				Context context) throws IOException, InterruptedException {
			context.write(key, info);
		}
	}

	public static class ABMapper extends Mapper<Object, Text, Text, Text> {
		Text word1 = new Text();
		Text word2 = new Text();

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
                return;  
            }
			StringTokenizer token1 = new StringTokenizer(linetemp);
			String line = "";
			if(token1.hasMoreTokens()) {
				line = token1.nextToken();
			}
			while(token1.hasMoreTokens()) {
				line = line + " " + token1.nextToken();
			}
			String str[] = line.split(" ");
			if(str[6].contains("@")) {
				str[6] = "0.5";
			}
			if(str[7].contains("*")) {
				str[7] = "0.5";
			}
			word1.set(str[0]);
			word2.set(str[1] + " " + str[2] + " " + str[3] + " " + str[4] + " " + str[5] + " " + str[6] + " " + str[7]);
			context.write(word1, word2);
		}
	}

	public static class ABReducer extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Text info,
				Context context) throws IOException, InterruptedException {
			context.write(key, info);
		}
	}

	public static class ACMapper extends Mapper<Object, Text, Text, Text> {
		Text word1 = new Text();
		Text word2 = new Text();

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
                return;  
            }
			StringTokenizer token1 = new StringTokenizer(linetemp);
			String line = "";
			if(token1.hasMoreTokens()) {
				line = token1.nextToken();
			}
			while(token1.hasMoreTokens()) {
				line = line + " " + token1.nextToken();
			}
			String str[] = line.split(" ");
			double usertag1 = 0;
			double usertag2 = 0;
			double usertag3 = 0;
			double usertag4 = 0;
			double usertag5 = 0;
			double songtag1 = Double.parseDouble(str[1]);
			double songtag2 = Double.parseDouble(str[2]);
			double songtag3 = Double.parseDouble(str[3]);
			double songtag4 = Double.parseDouble(str[4]);
			double songtag5 = Double.parseDouble(str[5]);
			double distance = 0;
			FileReader reader = new FileReader("pattern");
			BufferedReader br = new BufferedReader(reader);
			String s = null;
			while((s = br.readLine()) != null) {
				if(s.contains("%")) {
					StringTokenizer token2 = new StringTokenizer(s);
					String temp1 = "";
					if(token2.hasMoreTokens()) {
						temp1 = token2.nextToken();
					}
					while(token2.hasMoreTokens()) {
						temp1 = temp1 + " " + token2.nextToken();
					}
					String check[] = temp1.split(" ");
					usertag1 = Double.parseDouble(check[2]);
					usertag2 = Double.parseDouble(check[3]);
					usertag3 = Double.parseDouble(check[4]);
					usertag4 = Double.parseDouble(check[5]);
					usertag5 = Double.parseDouble(check[6]);
					break;
				}
			}
			double square1 = (songtag1 - usertag1) * (songtag1 - usertag1);
			double square2 = (songtag2 - usertag2) * (songtag2 - usertag2);
			double square3 = (songtag3 - usertag3) * (songtag3 - usertag3);
			double square4 = (songtag4 - usertag4) * (songtag4 - usertag4);
			double square5 = (songtag5 - usertag5) * (songtag5 - usertag5);
			double square = square1 + square2 + square3 + square4 + square5;
			int itr = 10000;
			double low = 0;
			double high = 18;
			double mean = (low + high) / 2;
			while(itr >= 0 && ((mean * mean - square) > 0.0001 || (mean * mean - square) < -0.0001)) {
				if(mean * mean < square) {
					low = mean;
				}
				else {
					high = mean;
				}
				mean = (low + high) / 2;
				itr--;			
			}
			double artist = 10 * Double.parseDouble(str[6]);
			double album = 10 * Double.parseDouble(str[7]);
			distance = mean + artist + album;
			String dis = String.valueOf(distance);
			word1.set(str[0]);
			word2.set(dis);
			context.write(word2, word1);
		}
	}

	public static class ACReducer extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Text info,
				Context context) throws IOException, InterruptedException {
			context.write(key, info);
		}
	}

	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] other = new GenericOptionsParser(conf, args).getRemainingArgs();

		Path del1 = new Path("temp1");
		Path del2 = new Path("temp2");
		Path del3 = new Path("output");
		FileSystem hdfs = FileSystem.get(conf);
		hdfs.delete(del1, true);
		hdfs.delete(del2, true);
		hdfs.delete(del3, true);

		DistributedCache.createSymlink(conf);
		DistributedCache.addCacheFile(new URI("temp.txt"+"#pattern"), conf);

		Job job1 = new Job(conf, "AA");
		job1.setJarByClass(RecommendationTwo.class);
		job1.setMapperClass(AAMapper.class);
		job1.setCombinerClass(AAReducer.class);
		job1.setReducerClass(AAReducer.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job1, new Path("input"));
		FileOutputFormat.setOutputPath(job1, new Path("temp1"));
		job1.waitForCompletion(true);

		Job job2 = new Job(conf, "AB");
		job2.setJarByClass(RecommendationTwo.class);
		job2.setMapperClass(ABMapper.class);
		job2.setCombinerClass(ABReducer.class);
		job2.setReducerClass(ABReducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job2, new Path("temp1"));
		FileOutputFormat.setOutputPath(job2, new Path("temp2"));
		job2.waitForCompletion(true);

		Job job3 = new Job(conf, "AC");
		job3.setJarByClass(RecommendationTwo.class);
		job3.setMapperClass(ACMapper.class);
		job3.setCombinerClass(ACReducer.class);
		job3.setReducerClass(ACReducer.class);
		job3.setOutputKeyClass(Text.class);
		job3.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job3, new Path("temp2"));
		FileOutputFormat.setOutputPath(job3, new Path("output"));
		job3.waitForCompletion(true);

		System.exit(1);
	}
}