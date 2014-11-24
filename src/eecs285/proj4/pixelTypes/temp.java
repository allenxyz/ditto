/*
 * Raster を生成する実験
 */

import java.awt.*;
import java.awt.image.*;

public class temp extends Canvas {

  /** BufferedImage のオブジェクト */
    public BufferedImage image;

  /** コンストラクタ */
    public temp() {
        super();
        setSize( 256, 256 );
        image = new BufferedImage( 2, 2, BufferedImage.TYPE_INT_RGB );
        WritableRaster raster = image.getRaster();
        int[] data = new int[3];
        data[0]=0; data[1]=0; data[2]=0;
        raster.setPixel( 0, 0, data );
        data[0]=255; data[1]=0; data[2]=0;
        raster.setPixel( 1, 0, data );
        data[0]=0; data[1]=255; data[2]=0;
        raster.setPixel( 0, 1, data );
        data[0]=0; data[1]=0; data[2]=255;
        raster.setPixel( 1, 1, data );

        DataBuffer buffer = raster.getDataBuffer();
        System.out.println( buffer.toString() );
        SampleModel model = raster.getSampleModel();
        System.out.println( model.toString() );
    }

  /** 描画メソッド */

    public void paint( Graphics g ) {

        g.drawImage( image, 0, 0, 256, 256, this );
    }

  /** 最初に呼び出されるメソッド */

    public static void main( String argv[] ) {

        Frame frame = new Frame( "Direct Color Model Test" );
        temp canvas = new temp(); 
        frame.add( canvas, "Center" );
        frame.pack();
        frame.setVisible( true );
    }
}