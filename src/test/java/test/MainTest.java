package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.gj.diary.utils.DESUtil;
import com.gj.diary.utils.DiaryUtil;

public class MainTest {

	public static void main1( String[] args ) throws Exception {
		String diaryMessage = DiaryUtil.getDiaryMessage( "F://temp//test2.jpg" );
		String md532 = MD5Util.getMd532( diaryMessage );
		md532 = md532.substring( 0, 24 );
		System.out.println( md532 );

//		ImageUtil imgCom = new ImageUtil( "F://temp//test2.jpg","ddd" );
//		imgCom.resizeFix( 0, 0 );

		String text = DESUtil.Encrypt( diaryMessage, md532.getBytes() );
		System.out.println( text );

		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( new File( "F://temp//temp.jpg" ), true ) ) );
		bufferedWriter.newLine();
		bufferedWriter.write( "=======================================================================" );
		bufferedWriter.newLine();
		bufferedWriter.write( text );
		bufferedWriter.close();

		String message = DiaryUtil.getDiaryMessage( "F://temp//temp.jpg" );
		System.out.println( message );
		System.out.println( "====================" );
		// AB37FD74A279C7C12B9EA416
		message = message.replaceAll( "\r|\n", "" );
		System.out.println( message );
		System.out.println( "====================" );
		String decrypt = DESUtil.Decrypt( message, "AB37FD74A279C7C12B9EA416".getBytes() );
		System.out.println( decrypt );
	}

	public static void main2( String[] args ) throws Exception {
		FileInputStream in = new FileInputStream( "F://temp//temp.jpg" );
		File file = new File( "F://temp//temp1.jpg" );
		if( !file.exists() )
			file.createNewFile();
		BufferedWriter bufferedWriter = null;
		bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file ) ) );
		@SuppressWarnings("unused")
		int c;
		byte buffer[] = new byte[1024];
		while( ( c = in.read( buffer ) ) != -1 ) {
			bufferedWriter.write( DESUtil.Encrypt( buffer, "123456781234567812345678".getBytes() ) );
			bufferedWriter.newLine();
		}
		in.close();
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	public static void main( String[] args ) throws Exception {
		BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( "F://temp//temp1.jpg" ) ) );
		File file = new File( "F://temp//temp3.jpg" );
		if( !file.exists() )
			file.createNewFile();
		FileOutputStream out = new FileOutputStream( file );
		String tempString = null;
		int i=0;
		while( ( tempString = reader.readLine() ) != null ) {
			out.write( DESUtil.Decrypt1( tempString, "123456781234567812345678".getBytes() ) );
		   if(i++ == 20){
			   break;
		   }
		}
		reader.close();
		out.flush();
		out.close();
	}
	
	public static void main4( String[] args ) throws Exception {
//		String aa ="ss\nddddddd\ncccccc";
//		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(aa.getBytes());
//		String[] split = aa.split( "\n" );
//		for(String ss :split ){
//			System.out.println( ss );
//		}
		
	}
}
