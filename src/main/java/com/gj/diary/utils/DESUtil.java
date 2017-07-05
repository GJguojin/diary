package com.gj.diary.utils;



import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class DESUtil {
	private static final String Algorithm = "DESede"; // 定义 加密算法,3DES
	
	public static String encrypt(byte[] str, byte[] key,String returnType) {
		byte[] encrypt =null;
		try {
			encrypt = encryptMode(key, str);
		} catch(Exception e ) {
			e.printStackTrace();
		}
		if(returnType == null || "hex".equals(returnType)){
			return byte2hex(encrypt);
		}else{
			String string = new String(Base64.getEncoder().encode(encrypt));
			return string;
		}
	}
	
	public static String encrypt(String str, byte[] key,String returnType) {
		try {
			return encrypt(str.getBytes("UTF-8"),key,returnType);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	// 解密
	public static byte[] decrypt(String str, byte[] key,String returnType) {
		try {
			byte[] srcByte = null;
			if(returnType == null ||  "hex".equals(returnType)){
				srcByte = hex2byte(str);
			}else{
				srcByte = Base64.getDecoder().decode(str.getBytes());
			}
			byte[] decrypt = decryptMode(key, srcByte);
			return decrypt;
		} catch(Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte[] b) { // 一个字节的数，
		// 转成16进制字符串
		String hs = "";
		String tmp = "";
		for (byte element : b) {
			tmp = (java.lang.Integer.toHexString(element & 0XFF));
			if (tmp.length() == 1) {
				hs = hs + "0" + tmp;
			} else {
				hs = hs + tmp;
			}
		}
		tmp = null;
		return hs.toUpperCase(); // 转成大写
	}

	/**
	 * 
	 * 字符串转java字节码
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] hex2byte(String str) {
		byte[] b = str.getBytes();
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;

	}

	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		}
		catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		}
		catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyte为加密密钥，长度为24字节

	// src为加密后的缓冲区

	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		}
		catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		}
		catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}

		return null;

	}

	public static byte[] Str2Hex(String str) {
		char[] ch = str.toCharArray();
		byte[] b = new byte[ch.length / 2];
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == 0) {
				break;
			}
			if (ch[i] >= '0' && ch[i] <= '9') {
				ch[i] = (char) (ch[i] - '0');
			} else if (ch[i] >= 'A' && ch[i] <= 'F') {
				ch[i] = (char) (ch[i] - 'A' + 10);
			}
		}
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (((ch[2 * i] << 4) & 0xf0) + (ch[2 * i + 1] & 0x0f));
		}
		return b;
	}

	/**
	 * 将byte数组转换为可见的字符串
	 * 
	 * @param b
	 *            待转换的byte数组
	 * @return 转换后的字符串
	 */
	public static String Hex2Str(byte[] b) {
		StringBuffer d = new StringBuffer(b.length * 2);
		for (byte element : b) {
			char hi = Character.forDigit((element >> 4) & 0x0F, 16);
			char lo = Character.forDigit(element & 0x0F, 16);
			d.append(Character.toUpperCase(hi));
			d.append(Character.toUpperCase(lo));
		}
		return d.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.out.println("郭晋".getBytes().length);
//		System.out.println("郭晋".getBytes("GBK").length);
//		System.out.println(new String("郭晋".getBytes("GBK"),"GBK"));
//		System.out.println(Charset.defaultCharset());
//		System.out.println( Hex2Str("guojin910707E4B8ADE59BBD".getBytes()) );
//		String key = "123456789012345678901234";  //key
//		System.out.println(key);
//		String s1 = DESUtil.Encrypt("caiban360", key.getBytes()); //加密
//		System.out.println(s1);
		String S2 =new String( DESUtil.decrypt("91720D594D32529924B3FBAD6FB6D04A0D910ABFFD722C2A812259D45B5EC85F357E795C0C85E39D1BDCA40FFB47D3534F89C6B6600DEBB70984C4343837DF663F807696352FA21BA4B7232BCF9EF8A7C654328C43824492FAEB88F0F439374E6AA151B975D13161C884A548594BE9E5BF2B2C19029536328A3007FA53F1E321C9C27723724C6057B95C5FDEC0E77C0AD0B2A19ADE2A6BC651B9080B5A85B76CBF83315E5BEC6DD5C11F05D9E1A0C8D7CF865A46030D78C3E876D80A1D88896076CCB57ED615DF7729007BFD1157C6005BD40DF6D73B29A81C8EA7BA4E00B1F40EF6104A1C430AD5525CD4CA228C9C7F086D10C9CC064749E669F452ACB770FFC45F0DEEB6EDC2259885C30E42C7999603BCBF66085868E0", //解密
				"1AB4007D8C29DECE7F53CE84".getBytes(),null));
		System.out.println(S2);
//		String  s3=DESUtil.Decrypt("29B9E1CED471CEBA", key.getBytes());
//		System.out.println(s3);
//		String k  =String.valueOf(new Date().getTime());
//		//DesKit.Encrypt(k, key.getBytes());
//		System.out.println(DESUtil.Encrypt(k, key.getBytes()));
	}

}
