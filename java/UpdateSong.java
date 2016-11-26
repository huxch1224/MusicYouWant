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

public class UpdateSong {

	public static class CollectMapper extends Mapper<Object, Text, Text, Text> {
		private Text songID = new Text();
		private Text songInfo = new Text();

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
				return;
		}
			StringTokenizer token = new StringTokenizer(linetemp);
			while(token.hasMoreTokens()) {
				String p = token.nextToken();
				if(p.equals("*")) {
					songID.set(token.nextToken());
					String s = token.nextToken() + " " + token.nextToken() + " " + token.nextToken() + " " + token.nextToken() + " " + token.nextToken() + " " + "1";
					songInfo.set(s);
					context.write(songID, songInfo);
				}
			}
		}
	}

	public static class CollectReducer extends Reducer<Text, Text, Text, Text> {
		private Text songInfo = new Text();

		public void reduce(Text songID, Iterable<Text> info,
				Context context) throws IOException, InterruptedException {
			int count = 0;
			int v1 = 0;
			int v2 = 0;
			int v3 = 0;
			int v4 = 0;
			int v5 = 0;
			String sum = "";
			for(Text v : info) {
				StringTokenizer temp = new StringTokenizer(v.toString());
				if(temp.hasMoreTokens()) {
					v1 = v1 + Integer.parseInt(temp.nextToken());
					v2 = v2 + Integer.parseInt(temp.nextToken());
					v3 = v3 + Integer.parseInt(temp.nextToken());
					v4 = v4 + Integer.parseInt(temp.nextToken());
					v5 = v5 + Integer.parseInt(temp.nextToken());
					count = count + Integer.parseInt(temp.nextToken());
				}
			}
			String result = String.valueOf(v1) + " " + String.valueOf(v2) + " " + String.valueOf(v3) + " " + String.valueOf(v4) + " " + String.valueOf(v5) + " " + String.valueOf(count);
			songInfo.set(result);
			context.write(songID, songInfo);
		}
	}

	public static class CountMapper extends Mapper<Object, Text, Text, Text> {
		private Text songID = new Text();
		private Text songInfo = new Text();

		public void map(Object key, Text val, Context context)
				throws IOException, InterruptedException {
			String linetemp = val.toString();
			if(linetemp == null || linetemp.equals("")) {  
				return;
			}
			StringTokenizer token1 = new StringTokenizer(linetemp);
			String a = token1.nextToken();

			if(token1.hasMoreTokens()) {
				FileReader reader = new FileReader("pattern");
				BufferedReader br = new BufferedReader(reader);
				String s  = br.readLine();
				while(s != null) {
					StringTokenizer token2 = new StringTokenizer(s);
					if(token2.hasMoreTokens()) {
						String b = token2.nextToken();
						if(a.equals(b)) {
							songID.set(b);
							double v1 = Double.parseDouble(token2.nextToken());
							double v2 = Double.parseDouble(token2.nextToken());
							double v3 = Double.parseDouble(token2.nextToken());
							double v4 = Double.parseDouble(token2.nextToken());
							double v5 = Double.parseDouble(token2.nextToken());
							int count = Integer.parseInt(token2.nextToken());
							String result = String.valueOf(v1 * count) + " " + String.valueOf(v2 * count) + " " + String.valueOf(v3 * count) + " " + String.valueOf(v4 * count) + " " + String.valueOf(v5 * count) + " " + String.valueOf(count);
							songInfo.set(result);
							context.write(songID, songInfo);
							break;
						}
					}
					s = br.readLine();
				}
				br.close();
			}
			songID.set(a);
			String str = token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken();
			songInfo.set(str);
			context.write(songID, songInfo);
		}
	}

	public static class CountReducer extends Reducer<Text, Text, Text, Text> {
		private Text songInfo = new Text();

		public void reduce(Text songID, Iterable<Text> info,
				Context context) throws IOException, InterruptedException {
			double v1 = 0;
			double v2 = 0;
			double v3 = 0;
			double v4 = 0;
			double v5 = 0;
			int count = 0;
			for(Text v : info) {
				StringTokenizer temp = new StringTokenizer(v.toString());
				if(temp.hasMoreTokens()) {
					double tempv1 = Double.parseDouble(temp.nextToken());
					double tempv2 = Double.parseDouble(temp.nextToken());
					double tempv3 = Double.parseDouble(temp.nextToken());
					double tempv4 = Double.parseDouble(temp.nextToken());
					double tempv5 = Double.parseDouble(temp.nextToken());
					int tempCount = Integer.parseInt(temp.nextToken());
					count = count + tempCount;
					v1 = v1 + tempv1;
					v2 = v2 + tempv2;
					v3 = v3 + tempv3;
					v4 = v4 + tempv4;
					v5 = v5 + tempv5;

				}
			}
			String result = String.valueOf(v1) + " " + String.valueOf(v2) + " " + String.valueOf(v3) + " " + String.valueOf(v4) + " " + String.valueOf(v5) + " " + String.valueOf(count);
			songInfo.set(result);
			context.write(songID, songInfo);
		}
	}

	 public static class FinalMapper extends Mapper<Object, Text, Text, Text> {
	 private Text songID = new Text();
	 private Text songInfo = new Text();
	 private String remove = "";

	 	public void map(Object key, Text val, Context context)
	 		throws IOException, InterruptedException {

	 		String linetemp = val.toString();
	 		if(linetemp == null || linetemp.equals("")) {
	 			return;  
	 		}
	 		StringTokenizer token1 = new StringTokenizer(linetemp);
	 		if(token1.hasMoreTokens()) {
	 			String a = token1.nextToken();
	 			double v1 = Double.parseDouble(token1.nextToken());
				double v2 = Double.parseDouble(token1.nextToken());
				double v3 = Double.parseDouble(token1.nextToken());
				double v4 = Double.parseDouble(token1.nextToken());
				double v5 = Double.parseDouble(token1.nextToken());
				int count = Integer.parseInt(token1.nextToken());
				v1 = v1 / count;
				v2 = v2 / count;
				v3 = v3 / count;
				v4 = v4 / count;
				v5 = v5 / count;

		 		FileReader reader = new FileReader("pattern");
		 		BufferedReader br = new BufferedReader(reader);
		 		String s = br.readLine();
		 		while(s != null) {
		 			StringTokenizer token2 = new StringTokenizer(s);
		 			if(token2.hasMoreTokens()) {
		 				String b = token2.nextToken();
		 				if(a.equals(b)) {
		 					token2.nextToken();
		 					token2.nextToken();
		 					token2.nextToken();
		 					token2.nextToken();
		 					token2.nextToken();
		 					token2.nextToken();
		 					remove = remove + " " + a;
		 					songID.set(a);
		 					String result = String.valueOf(v1) + " " + String.valueOf(v2) + " " + String.valueOf(v3) + " " + String.valueOf(v4) + " " + String.valueOf(v5) + " " + String.valueOf(count) + " " + token2.nextToken() + " " + token2.nextToken();
		 					songInfo.set(result);
		 					context.write(songID, songInfo);
		 					break;
		 				}
		 			}
		 			s  = br.readLine();
		 		}
		 		br.close();
		 	}
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			FileReader reader = new FileReader("pattern");
			BufferedReader br = new BufferedReader(reader);
			String s = br.readLine();
			while(s != null) {
				boolean check = true;
				StringTokenizer token1 = new StringTokenizer(s);
				if(token1.hasMoreTokens()) {
					String a = token1.nextToken();
					StringTokenizer token2 = new StringTokenizer(remove);
					while(token2.hasMoreTokens()) {
						String b = token2.nextToken();
						if(a.equals(b)) {
							check = false;
							break;
						}
					}
					if(check) {
						songID.set(a);
						String result = token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken() + " " + token1.nextToken();
						songInfo.set(result);
						context.write(songID, songInfo);
					}
				}
				s = br.readLine();
			}
			br.close();
		}
	}

	public static class FinalReducer extends Reducer<Text, Text, Text, Text> {
		private Text songInfo = new Text();

		public void reduce(Text songID, Text info,
				Context context) throws IOException, InterruptedException {
			context.write(songID, info);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] other = new GenericOptionsParser(conf, args).getRemainingArgs();

		Path delef1 = new Path("temp1");
		Path delef2 = new Path("temp2");
		Path delef3 = new Path("output");
		FileSystem hdfs = FileSystem.get(conf);
		hdfs.delete(delef1, true);
		hdfs.delete(delef2, true);
		hdfs.delete(delef3, true);

		DistributedCache.createSymlink(conf);
		DistributedCache.addCacheFile(new URI("song.txt"+"#pattern"), conf);

		Job job1 = new Job(conf, "Collect");
		job1.setJarByClass(UpdateSong.class);
		job1.setMapperClass(CollectMapper.class);
		job1.setCombinerClass(CollectReducer.class);
		job1.setReducerClass(CollectReducer.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job1, new Path("input"));
		FileOutputFormat.setOutputPath(job1, new Path("temp1"));
		job1.waitForCompletion(true);

		Job job2 = new Job(conf, "Count");
		job2.setJarByClass(UpdateSong.class);
		job2.setMapperClass(CountMapper.class);
		job2.setCombinerClass(CountReducer.class);
		job2.setReducerClass(CountReducer.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job2, new Path("temp1"));
		FileOutputFormat.setOutputPath(job2, new Path("temp2"));
		job2.waitForCompletion(true);

		Job job3 = new Job(conf, "Final");
		job3.setJarByClass(UpdateSong.class);
		job3.setMapperClass(FinalMapper.class);
		job3.setCombinerClass(FinalReducer.class);
		job3.setReducerClass(FinalReducer.class);
		job3.setOutputKeyClass(Text.class);
		job3.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job3, new Path("temp2"));
		FileOutputFormat.setOutputPath(job3, new Path("output"));
		job3.waitForCompletion(true);

		//hdfs.delete(delef1, true);
		//hdfs.delete(delef2, true);
		System.exit(1);
	}
}