package app.zingo.employeemanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.zingo.employeemanagement.R;

public class CreatePaySlip extends AppCompatActivity {

    private static TextInputEditText mName,mDesignation,mEId,mPAN,mMonth,mYear,mDepartment,mLeaveTaken,
                    mBasic,mHouse,mConvey,mMedical,mVehicle,mWashing,mOther,mOthers,mPF,mESI,mLoan,mPT,mLeaves,mAdvance;

    TextInputEditText mDOJ;

    private static AppCompatButton mCreate;

    String name,design,eid,pan,month,year,doj,dept,leaveTaken,basic,house,convey,medical,vehicle,washing,other,others,pf,esi,loan,pt,leaves,advance;

    double addition=0,deduction=0,net=0;

    //invoice
    String invoicepdfFilename = "";
    String invoicePdf;
    String invoicepdfFile,mGSTNumber;

    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_create_pay_slip);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Pay Slip");


            mName = (TextInputEditText)findViewById(R.id.employee_name);
            mDesignation = (TextInputEditText)findViewById(R.id.employee_desgination);
            mEId = (TextInputEditText)findViewById(R.id.employee_id);
            mPAN = (TextInputEditText)findViewById(R.id.employee_pan);
            mMonth = (TextInputEditText)findViewById(R.id.salary_month);
            mYear = (TextInputEditText)findViewById(R.id.salary_year);
            mDOJ = (TextInputEditText)findViewById(R.id.doj);
            mDepartment = (TextInputEditText)findViewById(R.id.department);
            mLeaveTaken = (TextInputEditText)findViewById(R.id.leave_taken);
            mBasic = (TextInputEditText)findViewById(R.id.basic_pay);
            mHouse = (TextInputEditText)findViewById(R.id.house_allow);
            mConvey = (TextInputEditText)findViewById(R.id.convey_allow);
            mMedical = (TextInputEditText)findViewById(R.id.medi_allow);
            mVehicle = (TextInputEditText)findViewById(R.id.vehicle_allow);
            mWashing = (TextInputEditText)findViewById(R.id.washing_allow);
            mOther = (TextInputEditText)findViewById(R.id.other_allow);
            mOthers = (TextInputEditText)findViewById(R.id.other_allows);
            mPF = (TextInputEditText)findViewById(R.id.pf);
            mESI = (TextInputEditText)findViewById(R.id.esi);
            mLoan = (TextInputEditText)findViewById(R.id.loan);
            mPT = (TextInputEditText)findViewById(R.id.pt);
            mLeaves = (TextInputEditText)findViewById(R.id.leaves);
            mAdvance = (TextInputEditText)findViewById(R.id.advance);


            mCreate = (AppCompatButton) findViewById(R.id.save);

            mDOJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        //launch datepicker modal
                        DatePickerDialog datePickerDialog = new DatePickerDialog(CreatePaySlip.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                        String from, to;
                                        Log.d("Date", "DATE SELECTED " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);


                                        String date1 = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                                        //String date2 = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");




                                            try {
                                                Date fdate = simpleDateFormat.parse(date1);
                                                Date fd = newDate.getTime();

                                                //cits = simpleDateFormat.format(fdate);
                                                from = sdf.format(fd);

                                                System.out.println("To = " + from);
                                                mDOJ.setText(from);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            //





                                    }
                                }, mYear, mMonth, mDay);


                        datePickerDialog.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            mCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validate();


                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void validate(){

        name = mName.getText().toString();
        design = mDesignation.getText().toString();
        eid = mEId.getText().toString();
        pan = mPAN.getText().toString();
        month = mMonth.getText().toString();
        year = mYear.getText().toString();
        doj = mDOJ.getText().toString();
        dept = mDepartment.getText().toString();
        leaveTaken = mLeaveTaken.getText().toString();
        basic = mBasic.getText().toString();
        house = mHouse.getText().toString();
        convey = mConvey.getText().toString();
        medical = mMedical.getText().toString();
        vehicle = mVehicle.getText().toString();
        washing = mWashing.getText().toString();
        other = mOther.getText().toString();
        others = mOthers.getText().toString();
        pf = mPF.getText().toString();
        esi = mESI.getText().toString();
        loan = mLoan.getText().toString();
        pt = mPT.getText().toString();
        leaves = mLeaves.getText().toString();
        advance = mAdvance.getText().toString();

        if(name==null||name.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(design==null||design.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(eid==null||eid.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(pan==null||pan.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(month==null||month.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(year==null||year.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(doj==null||doj.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(dept==null||dept.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(leaveTaken==null||leaveTaken.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(basic==null||basic.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(house==null||house.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(convey==null||convey.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(medical==null||medical.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(vehicle==null||vehicle.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(washing==null||washing.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(other==null||other.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }/*else if(others==null||others.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }*/else if(pf==null||pf.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(esi==null||esi.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(loan==null||loan.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(pt==null||pt.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(leaves==null||leaves.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else if(advance==null||advance.isEmpty()){

            Toast.makeText(this, "Field should not empty", Toast.LENGTH_SHORT).show();
        }else{

            double basics=0,houses=0,conveys=0,medicals=0,vehicles=0,washings=0,otherss=0,othersss=0,pfs=0,esis=0,loans=0,pts=0,leavess=0,advances=0;

            try{

                basics = Double.parseDouble(basic);
                houses = Double.parseDouble(house);
                conveys = Double.parseDouble(convey);
                medicals = Double.parseDouble(medical);
                vehicles = Double.parseDouble(vehicle);
                washings = Double.parseDouble(washing);
                otherss = Double.parseDouble(other);
              //  othersss = Double.parseDouble(others);

                pfs = Double.parseDouble(pf);
                esis = Double.parseDouble(esi);
                loans = Double.parseDouble(loan);
                pts = Double.parseDouble(pt);
                leavess = Double.parseDouble(leaves);
                advances = Double.parseDouble(advance);

                addition = basics+houses+conveys+medicals+vehicles+washings+otherss+othersss;
                deduction = pfs+esis+loans+pts+leavess+advances;

                if(pfs==0){
                    pf="-";
                }
                if(esis==0){
                    esi="-";
                }
                if(loans==0){
                    loan="-";
                }
                if(pts==0){
                    pt="-";
                }
                if(leavess==0){
                    leaves="-";
                }
                if(advances==0){
                    advance="-";
                }

                net = addition - deduction;


                try{

                    if((invoicepdfFile==null||invoicepdfFile.isEmpty())){
                        boolean fileCreated = createPDFInvoice();
                        if(fileCreated){
                            sendEmailattacheInvoice();
                        }else{
                            Toast.makeText(CreatePaySlip.this, "File not generate but booking happened", Toast.LENGTH_SHORT).show();
                            createPDFInvoice();
                        }

                    }else if((invoicepdfFile!=null&&!invoicepdfFile.isEmpty())){

                        sendEmailattacheInvoice();

                    }else{
                        boolean fileCreated = createPDFInvoice();
                        if(fileCreated){
                            sendEmailattacheInvoice();
                        }else{
                            Toast.makeText(CreatePaySlip.this, "File not generate but booking happened", Toast.LENGTH_SHORT).show();
                            createPDFInvoice();
                        }
                        // Toast.makeText(BillDetails.this, "Booking Not done", Toast.LENGTH_SHORT).show();
                    }
                    //createPDFInvoice();

                }catch (Exception e){
                    e.printStackTrace();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private boolean createPDFInvoice () throws Exception{

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            File sd = Environment.getExternalStorageDirectory();
            invoicePdf = System.currentTimeMillis() + ".pdf";

            File directory = new File(sd.getAbsolutePath()+"/Employee/Pdf/PaySlip/");
            //create directory if not exist
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }

            invoicepdfFile = sd.getAbsolutePath()+"/Employee/Pdf/PaySlip/"+invoicePdf;

            File file = new File(directory, invoicePdf);
            String path = "docs/" + invoicepdfFilename;
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(file));
            doc.addAuthor("Lucida Hospitality Pvt Ltd");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("zingohotels.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.LETTER);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            boolean beginPage = true;
            int y = 0;


            if(beginPage){
                generateLayoutInvoice(doc, cb);
                printPageNumber(cb);
                //doc.newPage();
                //generateLayoutNextInvoice(doc, cb);
                // printPageNumber(cb);
            }

            // printPageNumber(cb);
            return  true;

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
            return false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }

        }
    }

    private void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void generateLayoutInvoice(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy");
            SimpleDateFormat sdfs = new SimpleDateFormat("ddMMHHmm");
            SimpleDateFormat sdfd = new SimpleDateFormat("dd MMM");


            createHeadingsTitles(cb,250,760,"Lucida Hospitality Private Limited");
            createContentTitles(cb,300,740,"First Office, ZingoHotels- #88,1st Floor,");
            createContentTitles(cb,290,720,"Koramangala Industrial Layout 5th Block,");
            createContentTitles(cb,305,700,"Near JNC College, Bangalore-560095");
            createContentTitles(cb,335,680,"CIN: U55209KA2016PTC096968");
            createContentTitles(cb,355,660,"Email: hello@zingohotels.com");
            createContentTitles(cb,370,640,"Web: www.Zingohotels.com");
            createContentTitles(cb,390,620,"Mob: +91- 7065 651 651");

            createHeadingsTitles(cb,250,560,"Salary Slip");

            cb.rectangle(25,420,560,120);
            cb.stroke();

            createHeadings(cb,45,520,"Employee Name:");
            createHeadings(cb,45,500,"Designation:");
            createHeadings(cb,45,480,"Employee ID:");
            createHeadings(cb,45,460,"Income Tax PAN:");

            createContentTitles(cb,160,520,""+name);
            createContentTitles(cb,160,500,""+design);
            createContentTitles(cb,160,480,""+eid);
            createContentTitles(cb,160,460,""+pan);

            createHeadings(cb,390,520,"Month:");
            createHeadings(cb,390,500,"Year:");
            createHeadings(cb,390,480,"DOJ:");
            createHeadings(cb,390,460,"Department:");
            createHeadings(cb,390,440,"Leave Taken:");

            createContentTitles(cb,480,520,""+month);
            createContentTitles(cb,480,500,""+year);
            createContentTitles(cb,480,480,""+doj);
            createContentTitles(cb,480,460,""+dept);
            createContentTitles(cb,480,440,""+leaveTaken);

            cb.rectangle(50,160,510,240);
            cb.moveTo(210,400);
            cb.lineTo(210,160);
            cb.moveTo(310,400);
            cb.lineTo(310,160);
            cb.moveTo(450,400);
            cb.lineTo(450,160);
            cb.rectangle(50,380,510,20);
            cb.rectangle(50,160,510,40);
            cb.rectangle(310,200,250,40);
            cb.stroke();

            createHeadings(cb,80,385,"SALARY");
            createHeadings(cb,220,385,"AMOUNT Rs.");
            createHeadings(cb,320,385,"DEDUCTIONS");
            createHeadings(cb,460,385,"AMOUNT Rs.");

            createContentTitles(cb,60,365,"Basic Pay");
            createContentTitles(cb,60,345,"House Rent Allowance");
            createContentTitles(cb,60,325,"Conveyance Allowance");
            createContentTitles(cb,60,305,"Medical Allowance");
            createContentTitles(cb,60,285,"Vehicle Allowance");
            createContentTitles(cb,60,265,"Washing Allowance");
            createContentTitles(cb,60,245,"Other Allowance");


            createContentTitles(cb,320,365,"Provident Fund");
            createContentTitles(cb,320,345,"E.S.I");
            createContentTitles(cb,320,325,"Loan");
            createContentTitles(cb,320,305,"Advance");
            createContentTitles(cb,320,285,"Professional Tax");
            createContentTitles(cb,320,265,"Leaves");

            createHeadings(cb,220,365,""+basic);
            createHeadings(cb,220,345,""+house);
            createHeadings(cb,220,325,""+convey);
            createHeadings(cb,220,305,""+medical);
            createHeadings(cb,220,285,""+vehicle);
            createHeadings(cb,220,265,""+washing);
            createHeadings(cb,220,245,""+other);

            createHeadings(cb,460,365,""+pf);
            createHeadings(cb,460,345,""+esi);
            createHeadings(cb,460,325,""+loan);
            createHeadings(cb,460,305,""+advance);
            createHeadings(cb,460,285,""+pt);
            createHeadings(cb,460,265,""+leaves);

            createHeadings(cb,320,225,"Total Deductions");
            createHeadings(cb,460,225,""+new DecimalFormat("##,###.##").format(deduction));
            createHeadings(cb,80,180,"Gross Pay");
            createHeadings(cb,220,180,""+new DecimalFormat("##,###.##").format(addition));
            createHeadings(cb,350,180,"Net Pay");
            createHeadings(cb,460,180,""+new DecimalFormat("##,###.##").format(net));




            createContentTariff(cb,50,60, "*This is a computer generated salary slip and does not require a signature.",PdfContentByte.ALIGN_LEFT);


            //This is a computer generated invoice and does not require a signature.

            //Add logo_zingo
            Drawable d = getResources ().getDrawable (R.drawable.logo_zingo);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            ByteArrayOutputStream streams = new ByteArrayOutputStream();
            getResizedBitmap(bitmap,75,75).compress(Bitmap.CompressFormat.JPEG, 100, streams);
            byte[] bitmapData = streams.toByteArray();

            Image companyLogo = Image.getInstance(bitmapData);
            // Image companyLogo = Image.getInstance("logo_zingo_zingo.png");
            companyLogo.setAbsolutePosition(25,650);
            companyLogo.scalePercent(150);
            doc.add(companyLogo);
        }


        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 10, 0);
        cb.endText();

        pageNumber++;

    }

    private void createHeadingsTitles(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 18);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createHeadingsTitle(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 15);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void createContentTariff(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 14);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }
    private void createHeadings(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 12);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }
    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    private void createContentTitles(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bf, 14);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    private void sendEmailattacheInvoice() throws Exception {
        //String[] mailto = {mGuestEmail.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Pay Slip for"+month);
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "/Employee/Pdf/PaySlip/"+invoicePdf;
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            uri = FileProvider.getUriForFile(this, "app.zingo.employeemanagement.fileprovider", file);
        }else{
            uri = Uri.fromFile(file);
        }

        //Uri uri = Uri.fromFile(file);
        if(uri!=null){
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }else{
            Toast.makeText(CreatePaySlip.this, "File cannot access", Toast.LENGTH_SHORT).show();
        }
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:

                CreatePaySlip.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
