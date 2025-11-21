package com.elfadouaki.darelkilani;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.elfadouaki.darelkilani.adapter.VideoAdapter;
import com.elfadouaki.darelkilani.databinding.ActivityPageVideosBinding;
import com.elfadouaki.darelkilani.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class PageVideosActivity extends AppCompatActivity {

    private ActivityPageVideosBinding binding;
    private ImageButton btnBackPage;
    private EditText searchEditText;
    private VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPageVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int hizbNumber = getIntent().getIntExtra("hizb", 1);
        int thumunNumber = getIntent().getIntExtra("thumun", 1);

        List<VideoItem> videos = getVideoData(hizbNumber, thumunNumber);
        adapter = new VideoAdapter(videos);

        binding.videoList.setLayoutManager(new LinearLayoutManager(this));
        binding.videoList.setAdapter(adapter);

        binding.subtitleHeader.setText("الحزب " + getArabicNumber(hizbNumber) + " - الثمن " + getArabicNumber(thumunNumber));
        setTitle("دار الكيلاني");

        setupBackButton();
    }

    private void setupBackButton() {
        btnBackPage = findViewById(R.id.btnBackPage);
        btnBackPage.setOnClickListener(v -> finish());
    }


    private String getArabicNumber(int number) {
        String[] arabicNumbers = {"", "الأول", "الثاني", "الثالث", "الرابع", "الخامس", "السادس", "السابع", "الثامن"};
        return number < arabicNumbers.length ? arabicNumbers[number] : String.valueOf(number);
    }

    private List<VideoItem> getVideoData(int hizbNumber, int thumunNumber) {
        List<VideoItem> list = new ArrayList<>();
        String pageId = "100086541783034";

        if (hizbNumber == 1) {
            switch (thumunNumber) {
                case 1:
                    list.add(new VideoItem("773368364173093", "الثمن الأول من الحزب الأول من سورة البقرة برواية ورش عن نافع", pageId));
                    break;
                case 2:
                    list.add(new VideoItem("5484213488294332", "الثمن الثاني من الحزب الأول من سورة البقرة برواية ورش عن نافع", pageId));
                    list.add(new VideoItem("1097798900930882", "الثمن الثاني الحزب الأول من سورة البقرة برواية ورش عن نافع", pageId));
                    break;
                case 3:
                   list.add(new VideoItem("1048611856113662", "الثمن الثالث الحزب الأول سورة البقرة ورش عن نافع", pageId));
                    break;
                case 4:
                    list.add(new VideoItem("670774474739830", "تحفيظ سورة البقرة برواية ورش عن نافع بتكرار الآيات الحزب الأول الثمن الرابع", pageId));
                    break;
                case 5:
                    list.add(new VideoItem("821956639343999", "الثمن الخامس الحزب الأول رواية ورش عن نافع سورة البقرة", pageId));
                    list.add(new VideoItem("534624221609097", "تحفيظ سورة البقرة الحزب الأول الثمن الخامس برواية ورش عن نافع بالطريقة التسلسلية بتكرار الآيات لتعليم الأطفال", pageId));
                    break;
                case 6:
                    list.add(new VideoItem("3443529562548099", "تحفيظ سورة البقرة الحزب الأول الثمن السادس بالطريقة التسلسلية بتكرار الآيات", pageId));
                    break;
                case 7:
                    list.add(new VideoItem("2532984690193706", "الثمن السابع من الحزب الأول من سورة البقرة برواية ورش عن نافع", pageId));
                    list.add(new VideoItem("5000981643337704", "تحفيظ سورة البقرة الحزب الأول الثمن السابع برواية ورش عن نافع بتكرار الآيات المصحف المعلم", pageId));
                    break;
                case 8:
                    list.add(new VideoItem("568815758735415", "الثمن الثامن والأخير من الحزب الأول من سورة البقرة رواية ورش عن نافع", pageId));
                    list.add(new VideoItem("1715420065555551", "الثمن الثامن والأخير من الحزب الأول رواية ورش عن نافع سورة البقرة", pageId));
                    list.add(new VideoItem("2031178703939466", "الثمن الأخير من الحزب الأول من سورة البقرة برواية ورش عن نافع", pageId));
                    break;
            }
        } else if (hizbNumber == 2) {
            switch (thumunNumber) {
                case 1:
                    list.add(new VideoItem("2359835447515025", "سورة البقرة بداية الحزب الثاني الثمن الأول برواية ورش عن نافع", pageId));
                    break;
                case 2:
                    list.add(new VideoItem("457355863227352", "سورة البقرة. الثمن الثاني من الحزب الثاني برواية ورش عن نافع", pageId));
                    break;
                case 3:
                    list.add(new VideoItem("1158204705570976", "الثمن الثالث الحزب الثاني سورة البقرة رواية ورش عن نافع", pageId));
                    break;
                case 4:
                    list.add(new VideoItem("763154842224633", "الثمن الرابع الحزب الثاني سورة البقرة رواية ورش عن نافع", pageId));
                    list.add(new VideoItem("907395727301862", "الثمن الرابع من الحزب الثاني من سورة البقرة برواية ورش عن نافع", pageId));
                    break;
                case 5:
                    list.add(new VideoItem("812451273167331", "الثمن الخامس من الحزب الثاني من سورة البقرة برواية ورش عن نافع بتكرار الآيات لتعليم الصغار والكبار ولتثبيت الحفظ.", pageId));
                    list.add(new VideoItem("3111841472453690", "الثمن الخامس الحزب الثاني سورة البقرة رواية ورش عن نافع", pageId));
                    break;
                case 6:
                    list.add(new VideoItem("3043242879312764", "الثمن السادس من الحزب الثاني من سورة البقرة برواية ورش عن نافع", pageId));
                    break;
                case 7:
                    list.add(new VideoItem("547773393672116", "الثمن السابع من الحزب الثاني من سورة البقرة برواية ورش عن نافع بتكرار الآيات", pageId));
                    break;
                case 8:
                    list.add(new VideoItem("1406117866628768", "الثمن الثامن والأخير الحزب الثاني سورة البقرة رواية ورش عن نافع", pageId));
                    break;
            }
        } else if (hizbNumber == 3) {
            switch (thumunNumber) {
                case 1:
                    list.add(new VideoItem("940840167186758", "بداية الحزب الثالث الثمن الأول رواية ورش عن نافع سورة البقرة:( سيقول السفهاء)", pageId));
                    break;
                case 2:
                    list.add(new VideoItem("718372889851274", "الثمن الثاني من الحزب الثالث من سورة البقرة برواية ورش عن نافع بتكرار الآيات", pageId));
                    break;
                case 3:
                    list.add(new VideoItem("630249372414127", "الثمن الثالث الحزب الثالث رواية ورش عن نافع سورة البقرة", pageId));
                    break;
                case 4:
                    list.add(new VideoItem("1219703082065419", "الثمن الرابع الحزب الثالث سورة البقرة رواية ورش عن نافع", pageId));
                    list.add(new VideoItem("1418753988657676", "الثمن الرابع من الحزب الثالث من سورة البقرة برواية ورش عن نافع", pageId));
                    break;
                case 5:
                    list.add(new VideoItem("1209965049795631", "الثمن الخامس الحزب الثالث سورة البقرة رواية ورش عن نافع", pageId));
                    list.add(new VideoItem("711606027228236", "الثمن الخامس من الحزب الثالث حفظ سورة البقرة بالتكرار برواية ورش عن نافع", pageId));
                    break;
                case 6:
                    list.add(new VideoItem("945436139777496", "حفظ سورة البقرة بالتكرار الثمن السادس الحزب الثالث", pageId));
                    list.add(new VideoItem("792931952304134", "الثمن السادس الحزب الثالث سورة البقرة رواية ورش عن نافع", pageId));
                    break;
                case 7:
                    list.add(new VideoItem("1330100837717716", "الثمن السابع الحزب الثالث سورة البقرة رواية ورش عن نافع", pageId));
                    break;
                case 8:
                    list.add(new VideoItem("1141716269731863", "حفظ سورة البقرة بالتكرار الثمن الثامن والأخير الحزب الثالث", pageId));
                    break;
            }
        } else if (hizbNumber == 4) {
            switch (thumunNumber) {
                case 1:
                    list.add(new VideoItem("710509323956365", "حفظ سورة البقرة بالتكرار الثمن الأول الحزب الرابع", pageId));
                    break;
                case 2:
                    list.add(new VideoItem("1786129258450743", "الثمن الثاني الحزب الرابع حفظ سورة البقرة بالتكرار رواية ورش عن نافع", pageId));
                    break;
                case 3:
                    list.add(new VideoItem("316829367354301", "الثمن الثالث الحزب الرابع حفظ سورة البقرة بالتكرار رواية ورش عن نافع", pageId));
                    break;
                case 4:
                    list.add(new VideoItem("6607317785964824", "حفظ سورة البقرة بالتكرار الثمن الرابع  الحزب الرابع", pageId));
                    break;
                case 5:
                    list.add(new VideoItem("1904952056529648", "الثمن الخامس الحزب الرابع حفظ سورة البقرة بالتكرار رواية ورش عن نافع", pageId));
                    break;
                case 6:
                    list.add(new VideoItem("639822774740703", "الثمن السادس الحزب الرابع حفظ سورة البقرة بالتكرار رواية ورش عن نافع", pageId));
                    break;
                case 7:
                    list.add(new VideoItem("805365264654784", "الثمن السابع الحزب الرابع حفظ سورة البقرة بالتكرار رواية ورش عن نافع", pageId));
                    break;
                case 8:
                    list.add(new VideoItem("2532984690193706", "الثمن الثامن والأخير الحزب الرابع حفظ سورة البقرة بالتكرار رواية ورش عن نافع", pageId));
                    break;
            }
        } else if (hizbNumber == 5) {
            switch (thumunNumber) {
                case 1:
                    list.add(new VideoItem("6372628122756160", "بداية الحزب الخامس الثمن الأول حفظ سورة البقرة بالتكرار", pageId));
                    break;
                case 2:
                    list.add(new VideoItem("962627414743925", "حفظ سورة البقرة بالتكرار الثمن الثاني الحزب الخامس", pageId));
                    break;
                case 3:
                    list.add(new VideoItem("1281412399433460", "حفظ سورة البقرة بالتكرار الثمن الثالث الحزب الخامس رواية ورش عن نافع", pageId));
                    list.add(new VideoItem("1239819956952603", "حفظ سورة البقرة بالتكرار برواية ورش عن نافع الثمن الثالث الحزب الخامس", pageId));
                    break;
                case 4:
                    list.add(new VideoItem("247050584320553", "حفظ سورة البقرة بالتكرار الثمن الرابع الحزب الخامس", pageId));
                    break;
                case 5:
                    list.add(new VideoItem("711679064085356", "حفظ سورة البقرة بالتكرار الثمن الخامس الحزب الخامس", pageId));
                    list.add(new VideoItem("815078123280036", "حفظ سورة البقرة بالتكرار الثمن الخامس الحزب الخامس رواية ورش عن نافع", pageId));
                    break;
                case 6:
                    list.add(new VideoItem("1913642975664828", "حفظ سورة البقرة بالتكرار الثمن السادس الحزب الخامس رواية ورش عن نافع", pageId));
                    list.add(new VideoItem("685326836703747", "حفظ سورة البقرة بالتكرار الثمن السادس الحزب الخامس", pageId));
                    break;
                case 7:
                    list.add(new VideoItem("244022445086172", "حفظ سورة البقرة بالتكرار الثمن السابع والأخير الحزب الخامس رواية ورش عن نافع", pageId));
                    break;
            }
        }
        if (list.isEmpty()) {
            list.add(new VideoItem("", "لا توجد فيديوهات متاحة لهذا الثمن حالياً", pageId));
        }

        return list;
    }
}