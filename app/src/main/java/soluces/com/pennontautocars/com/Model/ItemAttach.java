package soluces.com.pennontautocars.com.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by RAYA on 10/02/2017.
 */

public class ItemAttach {
        private String name;
        private String code;
        private Drawable flag;
        public ItemAttach(String name, Drawable flag){
            this.name = name;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }


}
