package com.example.TVK.Controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.TVK.Model.User.Customer;
import com.example.TVK.Model.User.ICustomer;
import com.example.TVK.Model.User.IUser;
import com.example.TVK.Model.User.UserFactory;
import com.example.TVK.R;
import com.example.TVK.Ultis.IGetAPICallback;
import com.example.TVK.Ultis.IViewUltis;
import com.example.TVK.Ultis.MD5;
import com.example.TVK.Ultis.ValidateString;
import com.example.TVK.View.Adapter.CustomerAdapter;
import com.example.TVK.View.Fragment.IAddNewCustomer;
import com.example.TVK.View.Fragment.IListCustomer;
import com.example.TVK.View.IMainAdminActivity;

import java.util.ArrayList;

public class ManageCustomerController implements IManageCustomerController {
    IListCustomer iListCustomer;
    ICustomer customer = new Customer();
    IMainAdminActivity IadminActivity;
    IUser iUser;
    String userName, passWord, phone, otp_code, confirmPassword;
    IAddNewCustomer iAddNewCustomer;


    public ManageCustomerController(IListCustomer iListCustomer) {
        this.iListCustomer = iListCustomer;
    }

    public ManageCustomerController(IAddNewCustomer iAddNewCustomer) {
        this.iAddNewCustomer = iAddNewCustomer;
    }

    @Override
    public CustomerAdapter loadadapter(Context context) {
        CustomerAdapter customerAdapter;
        ArrayList<Customer> customerArrayList;
        customerArrayList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(context, R.layout.layout_custom_user, customerArrayList, new CustomerAdapter.IClickItemListener() {
            @Override
            public void onClickItemCustomer(Customer customer) {
                IadminActivity = (IMainAdminActivity) context;
                IadminActivity.gotoDetailFragment(customer);
            }
        });
        customer.getAllDataCustomer(context, customerArrayList, customerAdapter);
        return customerAdapter;
    }

    @Override
    public void OnCheckDataAndSendOTP(EditText txtUsername, EditText txtPassword, EditText txtConfirmPassword, EditText txtPhone, Context context) {
        userName = txtUsername.getText().toString().trim();
        passWord = txtPassword.getText().toString().trim();
        confirmPassword = txtConfirmPassword.getText().toString().trim();
        phone = txtPhone.getText().toString().trim();

        //userName empty
        if(TextUtils.isEmpty(userName))
        {
            txtUsername.requestFocus();
            txtUsername.setError("Kh??ng ???????c ????? tr???ng ph???n n??y");
            return;
        }
        else
        {
            //userName match available format
            if(!ValidateString.isValid(userName,"username"))
            {
                txtUsername.requestFocus();
                txtUsername.setError("T??n ????ng nh???p kh??ng ???????c c?? kho???ng tr???ng v?? k?? t??? ?????c bi???t v?? t??? 8-16 k?? t???");
                return;
            }
        }

        //passWord empty
        if(TextUtils.isEmpty(passWord))
        {
            txtPassword.requestFocus();
            txtPassword.setError("Kh??ng ???????c ????? tr???ng ph???n n??y");
            return;
        }
        else
        {
            //passWord match available format
            if(!ValidateString.isValid(passWord,"password"))
            {
                txtPassword.requestFocus();
                txtPassword.setError("M???t kh???u kh??ng ???????c c?? kho???ng tr???ng v?? k?? t??? ?????c bi???t v?? t??? 8-16 k?? t???");
                return;
            }
        }

        //confirm password empty
        if(TextUtils.isEmpty(confirmPassword))
        {
            txtConfirmPassword.requestFocus();
            txtConfirmPassword.setError("Kh??ng ???????c ????? tr???ng ph???n n??y");
            return;
        }
        else
        {
            //Confirm passWord not matched with password
            if(!confirmPassword.equals(passWord))
            {
                txtConfirmPassword.requestFocus();
                txtConfirmPassword.setError("X??c nh???n m???t kh???u ph???i kh???p v???i m???t kh???u");
                return;
            }
        }

        //phone emty
        if(TextUtils.isEmpty(phone))
        {
            txtPhone.requestFocus();
            txtPhone.setError("Kh??ng ???????c ????? tr???ng ph???n n??y");
            return;
        }
        else
        {
            //phone match available format
            if(!ValidateString.isValid(phone,"phone"))
            {
                txtPhone.requestFocus();
                txtPhone.setError("?????nh d???ng s??? ??i???n tho???i l?? +84 xxxx, nh???p ph???n xxxx");
                return;
            }
        }
        iUser = UserFactory.getUser("CUSTOMER");
        IViewUltis iViewUltis = (IViewUltis) this.iAddNewCustomer;
        IGetAPICallback iGetAPICallback = new RegisterController();
        iUser.getAllDataUser(context,iViewUltis,iGetAPICallback);
        //b???t dialogOTPLoading l??n

    }

    @Override
    public void OnAddNewCustomer(EditText txtUsername, EditText txtPassword, EditText txtPhone, String otp, Context context) {
        Customer new_customer = new Customer
                .CustomerBuilder().setUserName(txtUsername.getText().toString().trim())
                .setPassWord(MD5.HashMD5(txtPassword.getText().toString().trim()))
                .setPhone(txtPhone.getText().toString().trim())
                .setActivationCode(otp)
                .setStatus("consudung")
                .setTypeOfUser("CUSTOMER")
                .build();

        IViewUltis iViewUltis = (IViewUltis) this.iAddNewCustomer;

        IGetAPICallback iGetAPICallback = new RegisterController();

        new_customer.addNewCustomter(new_customer,context,iViewUltis,iGetAPICallback);
    }

}
