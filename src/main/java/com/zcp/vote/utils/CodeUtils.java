package com.zcp.vote.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CodeUtils {
	
	public static void generateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Random random = new Random();
		// 定义数组存放加减乘除四个运算符   
        // char[] arr = {'+' , '-', '*' , '/' };
		// 简化只使用加减两个运算符
        char[] arr = {'+' , '-'};
        // 生成10-20以内的随机整数num1   
        int num1 = random.nextInt(10) + 11;
        // 生成1-10以内的随机整数num2   
        int num2 = random.nextInt(10) + 1;   
        // 生成一个0-1之间的随机整数operate   
        int operate = random.nextInt(2);
        // 避免出现除数为0的情况   
//        if (operate == 3) {   
//            // 如果是除法, 那除数必须不能为0, 如果为0, 再次生成num2   
//            while (num2 == 0) {   
//                num2 = random.nextInt(10);   
//            }   
//        }
        // 运算结果   
        int result = 0;
        // 假定position值0/1/2/3分别代表”+”,”-“,”*”,”/”，计算前面操作数的运算结果   
        switch (operate) {   
	        case 0:   
	            result = num1 + num2;   
	            break;   
	        case 1:   
	            result = num1 - num2;   
	            break;   
	        case 2:   
	            result = num1 * num2;   
	            break;   
	        case 3:   
	            result = num1 / num2;   
	            break;   
        }   
        System.out.println(num1 + "," + num2 + "," + operate + "," + result);
        // 将生成的验证码值(即运算结果的值)放到session中，以便于后台做验证。   
        HttpSession session = request.getSession();   
        session.setAttribute("result", result);
        
        int width = 70, height = 20;   
        //创建BufferedImage对象，设置图片的长度宽度和色彩。   
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);   
        OutputStream os = null;
		try {
			os = response.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}   
        //取得Graphics对象，用来绘制图片   
        Graphics g = image.getGraphics();   
        //绘制图片背景和文字,释放Graphics对象所占用的资源。   
        g.setColor(getRandColor(200, 250));   
        //设置内容生成的位置   
        g.fillRect(0, 0, width, height);   
        //设置内容的字体和大小   
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));   
           
        //设置内容的颜色：主要为生成图片背景的线条   
        g.setColor(getRandColor(160, 200));   
           
        //图片背景上随机生成155条线条，避免通过图片识别破解验证码   
        for (int i = 0; i < 155; i++) {   
            int x = random.nextInt(width);   
            int y = random.nextInt(height);   
            int xl = random.nextInt(12);   
            int yl = random.nextInt(12);   
            g.drawLine(x, y, x + xl, y + yl);   
        }   
        //构造运算表达式   
        String content = num1+" "+ arr[operate]+" "+num2+" = ";   
        //设置写运算表达的颜色   
        g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));   
        //在指定位置绘制指定内容（即运算表达式）   
        g.drawString(content, 5, 16);   
        //释放此图形的上下文以及它使用的所有系统资源，类似于关闭流   
        g.dispose();   
        // No Expires
        response.setDateHeader("Expires", 0);  
        // Set standard HTTP/1.1 no-cache headers.  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        // Set standard HTTP/1.0 no-cache header.  
        response.setHeader("Pragma", "no-cache");  
        // return a jpeg  
        response.setContentType("image/jpeg");
        //通过ImageIO对象的write静态方法将图片输出。   
		ImageIO.write(image, "JPEG", os);
		os.close();
	}
	/**  
     * 生成随机颜色  
     * @param fc  
     * @param bc  
     * @return  
     */  
    private static Color getRandColor(int fc, int bc) {   
        Random random = new Random();   
        if (fc > 255)   
            fc = 255;   
        if (bc > 255)   
            bc = 255;   
        int r = fc + random.nextInt(bc - fc);   
        int g = fc + random.nextInt(bc - fc);   
        int b = fc + random.nextInt(bc - fc);   
        return new Color(r, g, b);   
    }
}
