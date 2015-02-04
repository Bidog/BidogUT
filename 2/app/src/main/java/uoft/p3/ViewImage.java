package uoft.p3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ViewImage extends Activity {
	// Declare Variable
	TextView text;
	ImageView imageview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from view_image.xml
		setContentView(R.layout.view_image);

		// Retrieve data from MainActivity on GridView item click
		Intent i = getIntent();

		// Get the position
		final int position = i.getExtras().getInt("position");

		// Get String arrays FilePathStrings
		String[] filepath = i.getStringArrayExtra("filepath");

		// Get String arrays FileNameStrings
		String[] filename = i.getStringArrayExtra("filename");

		// Locate the TextView in view_image.xml
		text = (TextView) findViewById(R.id.imagetext);

		// Load the text into the TextView followed by the position
		text.setText(filename[position]);

		// Locate the ImageView in view_image.xml
		imageview = (ImageView) findViewById(R.id.full_image_view);

		// Decode the filepath with BitmapFactory followed by the position
		Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

        int nh = (int) ( bmp.getHeight() * (512.0 / bmp.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 512, nh, true);
        // Set the decoded bitmap into ImageView
        imageview.setImageBitmap(scaled);

        if(bmp!=null && !bmp.isRecycled())
        {
            bmp.recycle();
            bmp=null;
        }
        Button mDeleteButton = (Button) findViewById(R.id.Delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    File directory = new File(
                android.os.Environment.getExternalStorageDirectory()
                        + File.separator + "wuyueCam");
                if (directory.isDirectory()) {
                    String[] children = directory.list();
                        new File(directory, children[position]).delete();
                }
                Intent explicitIntent = new Intent(ViewImage.this, galleryMode.class);
                startActivity(explicitIntent);
            }
        });
	}
}