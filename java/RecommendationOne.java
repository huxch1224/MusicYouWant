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

public class RecommendationOne {

	public static class AAMapper extends Mapper<Object, Text, Text, Text> {
		Text word1 = new Text();
		Text word2 = new Text();
		Text word3 = new Text();

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

			if(!line.contains("*")) {
				word1.set("%");
				word2.set(line);
				context.write(word1, word2);
				return;
			}

			Text word4 = new Text();
			Text word5 = new Text();
			word4.set("-");
			word5.set(str[1]);
			context.write(word4, word5);

			FileReader reader = new FileReader("pattern");
			BufferedReader br = new BufferedReader(reader);
			String s = null;
			while((s = br.readLine()) != null) {
				StringTokenizer token2 = new StringTokenizer(s);
				String temp1 = "";
				if(token2.hasMoreTokens()) {
					temp1 = token2.nextToken();
				}
				while(token2.hasMoreTokens()) {
					temp1 = temp1 + " " + token2.nextToken();
				}
				String check[] = temp1.split(" ");
				if(check[0].equals(str[1])) {
					word1.set(check[7] + "@");
					word2.set(str[2]);
					context.write(word1, word2);
					word3.set(check[8] + "*");
					context.write(word3, word2);
					return;
				}
			}
			br.close();
		}	
	}

	public static class AAReducer extends Reducer<Text, Text, Text, Text> {
		Text word1 = new Text();

		public void reduce(Text key, Iterable<Text> info,
				Context context) throws IOException, InterruptedException {
			String check = key.toString();
			if(check.contains("@")) {
				int count = 0;
				double artist = 0;
				for(Text val : info) {
					String y = val.toString();
					count++;
					artist = artist + Double.parseDouble(y);
				}
				artist = artist / count;
				word1.set(String.valueOf(artist));
				context.write(key, word1);
				return;
			}

			if(check.contains("*")) {
				int count = 0;
				double album = 0;
				for(Text val : info) {
					String y = val.toString();
					count++;
					album = album + Double.parseDouble(y);
				}
				album = album / count;
				word1.set(String.valueOf(album));
				context.write(key, word1);
				return;
			}

			if(check.contains("%") || check.contains("-")) {
				for(Text val : info) {
					String y = val.toString();
					word1.set(y);
					context.write(key, word1);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] other = new GenericOptionsParser(conf, args).getRemainingArgs();

		Path del1 = new Path("temp1");
		FileSystem hdfs = FileSystem.get(conf);
		hdfs.delete(del1, true);

		DistributedCache.createSymlink(conf);
		DistributedCache.addCacheFile(new URI("song.txt"+"#pattern"), conf);

		Job job1 = new Job(conf, "AA");
		job1.setJarByClass(RecommendationOne.class);
		job1.setMapperClass(AAMapper.class);
		job1.setCombinerClass(AAReducer.class);
		job1.setReducerClass(AAReducer.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job1, new Path("input"));
		FileOutputFormat.setOutputPath(job1, new Path("temp1"));
		job1.waitForCompletion(true);

		System.exit(1);
	}
}