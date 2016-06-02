package com.evansappwriter.ebook004;


import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class EBookSplashActivity extends EBookActivity {
	    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        startAnimating();
    }
	
	private void startAnimating() {
		ImageView bookCover = (ImageView)  findViewById(R.id.ImageViewCover);
		Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		bookCover.startAnimation(fade);
		
		fade.setAnimationListener(new AnimationListener() {
        	public void onAnimationEnd(Animation animation) {
        		startActivity(new Intent(EBookSplashActivity.this, EBookTableContentActivity.class));
        		EBookSplashActivity.this.finish();
        	}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
        }); 
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
	}

	@Override
	protected void onPause() {
		super.onPause();
		ImageView bookCover = (ImageView)  findViewById(R.id.ImageViewCover);
		bookCover.clearAnimation();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		startAnimating();
	}	
}