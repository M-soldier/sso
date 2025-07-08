<style>
.center {
  width: 100%;
  height: 100vh;
  background-image: url(../../assets/background.jpg);
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  overflow: auto;
}

h1 {
  font-size: 30px;
  color: black;
}

.logon {
  border-radius: 10px;
  box-shadow:
    0 14px 28px rgba(0, 0, 0, 0.25),
    0 10px 10px rgba(0, 0, 0, 0.22);
  /* position: raative;
  overflow: hidden; */
  width: 768px;
  max-width: 100%;
  min-height: 480px;
  margin-top: 20px;
  display: flex;
  /*background: #fff -webkit-linear-gradient(right, #4284db, #29eac4);*/
  background: linear-gradient(to left, #4284db, #29eac4);
}

.loginView {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.loginViewLeft {
  border-radius: 0 10px 10px 0;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(100%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.loginViewRight {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.registerView {
  border-radius: 0 10px 10px 0;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
}

.registerViewH2 {
  font-size: 30px;
  color: #fff;
  margin-top: 20px;
}

.registerViewP {
  font-size: 15px;
  color: #fff;
  margin-top: 20px;
}

.registerViewLeft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
}

.registerViewRight {
  border-radius: 0 10px 10px 0;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(-100%);
  transition: transform 0.6s ease-in-out;
}

.registerView-SignIn {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.registerView-SignUp {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.registerViewButton {
  width: 180px;
  height: 40px;
  border-radius: 50px;
  border: 1px solid #fff;
  color: #fff;
  font-size: 15px;
  text-align: center;
  line-height: 40px;
  margin-top: 40px;
}

.loginViewH2 {
  font-size: 25px;
  color: black;
  /* width: 250px; */
}

.loginView-SignIn {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.loginView-SignUp {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

input {
  background-color: #eee;
  border: none;
  padding: 12px 15px;
  margin: 10px 0;
  width: 240px;
}
h3 {
  font-size: 10px;
  margin-top: 10px;
  cursor: pointer;
}
.loginViewButton {
  background-color: #29eac4;
  border: none;
  width: 180px;
  height: 40px;
  border-radius: 50px;
  font-size: 15px;
  color: #fff;
  text-align: center;
  line-height: 40px;
  margin-top: 30px;
}

.a-form-item__label {
  margin-top: 10px;
}

.gender {
  margin-top: 10px;
}
</style>

<template>
  <div class="center">
    <h1 style="font-size: xxx-large">统一身份认证</h1>
    <div class="logon">
      <div :class="loginView">
        <div class="loginView-SignIn" v-show="mode === 0">
          <h2 class="loginViewH2">登录账户</h2>

          <a-form ref="loginRef" :model="loginForm" :rules="loginRules" status-icon>
            <a-form-item name="userAccountForLogin" has-feedback>
              <a-input
                type="text"
                v-model:value="loginForm.userAccountForLogin"
                autocomplete="off"
                placeholder="请输入手机号码"
              >
                <template #prefix>
                  <UserOutlined class="site-form-item-icon" />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item name="passwordForLogin" has-feedback>
              <a-input-password
                v-model:value="loginForm.passwordForLogin"
                autocomplete="off"
                placeholder="请输入密码"
              >
                <template #prefix>
                  <LockOutlined class="site-form-item-icon" />
                </template>
              </a-input-password>
            </a-form-item>
          </a-form>

          <h3 @click="forgetPassword">忘记密码?</h3>
          <button class="loginViewButton" @click="submitLoginForm">登录</button>
        </div>

        <div class="loginView-SignUp" v-show="mode === 1">
          <h2 class="loginViewH2">注册账户</h2>
          <a-form
            ref="registerRef"
            label-align="left"
            status-icon
            :model="registerForm"
            :rules="registerRules"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 16 }"
          >
            <a-form-item label="昵称" name="username" has-feedback>
              <a-input
                type="text"
                v-model:value="registerForm.username"
                autocomplete="off"
              ></a-input>
            </a-form-item>

            <a-form-item label="手机号码" name="userAccount" has-feedback>
              <a-input
                type="text"
                v-model:value="registerForm.userAccount"
                autocomplete="off"
              ></a-input>
            </a-form-item>

            <a-form-item label="密码" name="password" has-feedback>
              <a-input-password
                v-model:value="registerForm.password"
                autocomplete="off"
              ></a-input-password>
            </a-form-item>
            <a-form-item label="确认密码" name="checkPass" has-feedback>
              <a-input-password
                v-model:value="registerForm.checkPass"
                autocomplete="off"
              ></a-input-password>
            </a-form-item>
            <div style="display: flex; justify-content: space-between">
              <a-button
                style="
                  background-color: #29eac4;
                  border: none;
                  width: 120px;
                  height: 40px;
                  border-radius: 50px;
                  font-size: 15px;
                  color: #fff;
                  margin-top: 30px;
                "
                @click="submitRegisterForm()"
                >提交</a-button
              >
              <a-button
                style="
                  background-color: #29eac4;
                  border: none;
                  width: 120px;
                  height: 40px;
                  border-radius: 50px;
                  font-size: 15px;
                  color: #fff;
                  margin-top: 30px;
                "
                @click="resetRegisterForm()"
                >重置</a-button
              >
            </div>
          </a-form>
        </div>
      </div>
      <div :class="registerView">
        <div class="registerView-SignIn" v-show="mode === 0">
          <h2 class="registerViewH2">欢迎登录！</h2>
          <p class="registerViewP">请您填写手机号码和密码进行登录，</p>
          <p class="registerViewP">没有账号请点击下方按钮进行注册。</p>
          <div class="registerViewButton" @click="SignIn">注册</div>
        </div>
        <div class="registerView-SignUp" v-show="mode === 1">
          <h2 class="registerViewH2">欢迎注册！</h2>
          <p class="registerViewP">请您填写相关信息进行注册，</p>
          <p class="registerViewP">注册完毕请点击下方按钮进行登录。</p>
          <div class="registerViewButton" @click="SignUp">登录</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { login, register } from '@/api/user.ts'

import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { reactive, ref } from 'vue';
import type { Rule } from 'ant-design-vue/es/form';
import type { FormInstance } from 'ant-design-vue';

interface LoginFormState {
  userAccountForLogin: string;
  passwordForLogin: string;
}

interface RegisterFormState {
  username: string;
  userAccount: string;
  password: string;
  checkPass: string;
}

const loginView = ref<string>('loginView');
const registerView = ref<string>('registerView');


// mode为0表示登录页面，mode为1表示注册页面
const mode = ref<0|1>(0);
const SignIn = () => {
  loginView.value = 'loginViewLeft';
  registerView.value = 'registerViewRight';
  setTimeout(() => {
    mode.value = 1;
  }, 200)
};
const SignUp = () => {
  loginView.value = 'loginViewRight';
  registerView.value = 'registerViewLeft';

  setTimeout(() => {
    mode.value = 0
  }, 200)
};


const loginRef = ref<FormInstance>();
const loginForm = reactive<LoginFormState>({
  userAccountForLogin: '',
  passwordForLogin: '',
});

const registerRef = ref<FormInstance>();
const registerForm = reactive<RegisterFormState>({
  username: '',
  userAccount: '',
  password: '',
  checkPass: '',
});

const checkPhoneNum = async (_rule: Rule, value: string) => {
  console.log(value);
  if (value === '') {
    return Promise.reject('请输入手机号码');
  } else if (!/^1[34578]\d{9}$/.test(value)) {
    return Promise.reject('请输入正确的手机号码');
  } else {
    return Promise.resolve();
  }
};

const validateLoginPass = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请输入密码');
  } else if (value.length < 8 || value.length > 16) {
    return Promise.reject('密码长度不能小8位且不能大于16位');
  } else if (!/\d/.test(value) || !/[a-z]/.test(value) || !/[A-Z]/.test(value)) {
    return Promise.reject('密码中必须包含大小写字符和数字');
  } else {
    return Promise.resolve();
  }
};

const validatePass = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请输入密码');
  } else {
    if (registerForm.checkPass !== '') {
      registerRef.value?.validateFields('checkPass');
    }
    if (value.length < 8 || value.length > 16) {
      return Promise.reject('密码长度不能小于8位且不能大于16位');
    } else if (!/\d/.test(value) || !/[a-z]/.test(value) || !/[A-Z]/.test(value)) {
      return Promise.reject('密码中必须包含大小写字符和数字');
    }
    return Promise.resolve();
  }
};

const validatePass2 = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请再次输入密码');
  } else if (value !== registerForm.password) {
    return Promise.reject('两次输入密码不一致!');
  } else {
    return Promise.resolve();
  }
};

const loginRules: Record<string, Rule[]> = {
  userAccountForLogin: [{ validator: checkPhoneNum, trigger: 'blur', required: true }],
  passwordForLogin: [{validator: validateLoginPass, trigger: 'blur', required: true}],
};

const registerRules: Record<string, Rule[]> = {
  username: [{ min: 3, max: 8, trigger: 'blur', required: true, message: '昵称应为3-8个字符' }],
  userAccount: [{ validator: checkPhoneNum, trigger: 'blur', required: true }],

  password: [{ validator: validatePass, trigger: 'blur', required: true }],
  checkPass: [{ validator: validatePass2, trigger: 'blur', required: true }],
};

const forgetPassword = () => {
  alert('请联系管理员：msj@bupt.edu.cn');
}

const submitLoginForm = () => {
  loginRef.value?.validate().then(async () => {
    await login(loginForm.userAccountForLogin, loginForm.passwordForLogin).then((res) => {
      if (res.data.code === 20000) {
        console.log("登录结果", res.data.data)
        window.location.href = res.data.data;
      } else {
        alert(res.data.message);
      }
    }).catch((err) => {
      console.log(err);
      alert("登录失败");
    }).finally(() => {
      loginRef.value?.resetFields();
    })
  })
}

const submitRegisterForm = () => {
  registerRef.value?.validate().then(async () => {
    await register(registerForm.username, registerForm.userAccount, registerForm.password, registerForm.checkPass).then((res) => {
      if (res.data.code !== 20000) {
        alert(res.data.message);
      }else {
        alert('注册成功，请登录！');
      }
    }).catch(() => {
      console.log('error submit!!')
      alert("注册失败");
    }).finally(() => {
      loginView.value = 'loginViewRight';
      registerView.value = 'registerViewLeft';
      setTimeout(() => {
        mode.value = 0;
        resetRegisterForm();
      }, 200)
    })
  })


};

const resetRegisterForm = () => {
  registerRef.value?.resetFields();
}

</script>
