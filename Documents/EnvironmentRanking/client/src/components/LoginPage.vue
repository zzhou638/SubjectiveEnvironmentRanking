<template>
  <div class="login-container">
    <div class="login-background">
      <div class="login-card">
        <!-- iCloud风格的logo区域 -->
        <div class="logo-section">
          <div class="logo-icon">
            <svg viewBox="0 0 100 100" class="cloud-icon">
              <path d="M75 35c-1.5-8.5-8.5-15-17-15-6 0-11.5 3-14.5 8-2.5-1-5-1.5-7.5-1.5-10 0-18 8-18 18 0 2 0.5 4 1 6-7.5 1.5-13 8.5-13 16.5 0 9.5 7.5 17 17 17h50c8.5 0 15.5-7 15.5-15.5 0-7.5-5.5-14-12.5-15.5z" fill="#007AFF"/>
            </svg>
          </div>
          <h1 class="app-title">{{ texts.appTitle }}</h1>
          <p class="app-subtitle">{{ texts.appSubtitle }}</p>
        </div>

        <!-- 登录区域 -->
        <div class="login-section">
          <h2 class="login-title">{{ texts.welcomeTitle }}</h2>
          <p class="login-description">
            {{ texts.loginDescription }}
          </p>
          
          <button 
            class="login-button"
            @click="handleLogin"
            :disabled="isLoading"
          >
            <span v-if="!isLoading" class="button-content">
              <svg class="login-icon" viewBox="0 0 24 24">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM12 20c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z"/>
              </svg>
              {{ texts.loginButton }}
            </span>
            <span v-else class="loading-content">
              <div class="spinner"></div>
              {{ texts.loginLoading }}
            </span>
          </button>

          <!-- 错误消息显示 -->
          <div v-if="errorMessage" class="error-message">
            <svg class="error-icon" viewBox="0 0 24 24">
              <path d="M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z"/>
            </svg>
            {{ errorMessage }}
          </div>

          <div class="features">
            <div class="feature-item">
              <svg class="feature-icon" viewBox="0 0 24 24">
                <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4z"/>
              </svg>
              <span>{{ texts.feature1 }}</span>
            </div>
            <div class="feature-item">
              <svg class="feature-icon" viewBox="0 0 24 24">
                <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
              <span>{{ texts.feature2 }}</span>
            </div>
            <div class="feature-item">
              <svg class="feature-icon" viewBox="0 0 24 24">
                <path d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
              </svg>
              <span>{{ texts.feature3 }}</span>
            </div>
          </div>
        </div>

        <!-- 底部信息 -->
        <div class="footer">
          <p class="footer-text">
            {{ texts.footerText }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ApiService from '@/services/apiService'

export default {
  name: 'LoginPage',
  props: {
    language: {
      type: String,
      default: 'zh'
    }
  },
  data() {
    return {
      isLoading: false,
      errorMessage: ''
    }
  },
  computed: {
    texts() {
      const translations = {
        zh: {
          appTitle: '城中村环境主观评价系统',
          appSubtitle: 'Urban Village Environment Subjective Assessment System',
          welcomeTitle: '欢迎使用',
          loginDescription: '点击下方按钮即可开始体验我们的城中村环境评价服务',
          loginButton: '立即登录',
          loginLoading: '登录中...',
          feature1: '安全可靠',
          feature2: '操作便捷',
          feature3: '匿名收集',
          footerText: '使用匿名登录即表示您同意我们的服务条款和隐私政策'
        },
        en: {
          appTitle: 'Urban Village Environment Subjective Assessment System',
          appSubtitle: 'Urban Village Environment Assessment System',
          welcomeTitle: 'Welcome',
          loginDescription: 'Click the button below to start experiencing our urban village environment assessment service',
          loginButton: 'Login Now',
          loginLoading: 'Logging in...',
          feature1: 'Secure & Reliable',
          feature2: 'Easy to Use',
          feature3: 'Anonymous Collection',
          footerText: 'By using anonymous login, you agree to our Terms of Service and Privacy Policy'
        }
      };
      return translations[this.language];
    }
  },
  methods: {
    async handleLogin() {
      this.isLoading = true;
      this.errorMessage = '';
      
      try {
        // 使用统一的API服务
        const data = await ApiService.login({});
        
        // 检查响应消息
        if (data.message === '登录成功' || data.message === 'Login successful') {
          console.log(this.language === 'zh' ? '登录成功:' : 'Login successful:', data.message);
          // cookie会自动保存，因为ApiService设置了credentials: 'include'
          this.$emit('login-success');
        } else {
          throw new Error(data.message || (this.language === 'zh' ? '登录失败' : 'Login failed'));
        }
        
      } catch (error) {
        console.error(this.language === 'zh' ? '登录错误:' : 'Login error:', error);
        this.errorMessage = error.message || (this.language === 'zh' ? '网络连接失败，请检查后端服务是否启动' : 'Network connection failed, please check if the backend service is running');
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffffff;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  padding: 20px;
}

.login-background {
  width: 100%;
  max-width: 480px;
  perspective: 1000px;
}

.login-card {
  background: #ffffff;
  border-radius: 20px;
  padding: 60px 40px 40px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  animation: cardFloat 6s ease-in-out infinite;
}

@keyframes cardFloat {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-5px); }
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  margin-bottom: 20px;
}

.cloud-icon {
  width: 80px;
  height: 80px;
  filter: drop-shadow(0 2px 8px rgba(0, 122, 255, 0.2));
  animation: logoFloat 4s ease-in-out infinite;
}

@keyframes logoFloat {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-3px); }
}

.app-title {
  font-size: 24px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 8px 0;
  letter-spacing: -0.3px;
  line-height: 1.2;
}

.app-subtitle {
  font-size: 18px;
  color: #86868b;
  margin: 0;
  font-weight: 400;
}

.login-section {
  text-align: center;
  margin-bottom: 40px;
}

.login-title {
  font-size: 28px;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 16px 0;
}

.login-description {
  font-size: 16px;
  color: #86868b;
  line-height: 1.5;
  margin: 0 0 40px 0;
}

.login-button {
  width: 100%;
  padding: 16px 24px;
  background: #007AFF;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
  margin-bottom: 30px;
  box-shadow: 0 1px 3px rgba(0, 122, 255, 0.3);
}

.login-button:hover:not(:disabled) {
  background: #0056CC;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 122, 255, 0.3);
}

.login-button:active:not(:disabled) {
  transform: translateY(0);
  background: #004499;
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.button-content,
.loading-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-icon {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 16px;
  background: rgba(255, 59, 48, 0.1);
  border: 1px solid rgba(255, 59, 48, 0.2);
  border-radius: 8px;
  color: #ff3b30;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 20px;
  animation: errorSlideIn 0.3s ease-out;
}

.error-icon {
  width: 16px;
  height: 16px;
  fill: currentColor;
  flex-shrink: 0;
}

@keyframes errorSlideIn {
  0% {
    opacity: 0;
    transform: translateY(-10px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.features {
  display: flex;
  justify-content: space-around;
  gap: 16px;
  margin-top: 30px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  padding: 16px 8px;
  border-radius: 12px;
  background: rgba(0, 0, 0, 0.02);
  transition: all 0.2s ease;
}

.feature-item:hover {
  background: rgba(0, 0, 0, 0.04);
  transform: translateY(-1px);
}

.feature-icon {
  width: 24px;
  height: 24px;
  fill: #007AFF;
}

.feature-item span {
  font-size: 14px;
  color: #1d1d1f;
  font-weight: 500;
}

.footer {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
}

.footer-text {
  font-size: 12px;
  color: #86868b;
  line-height: 1.4;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 600px) {
  .login-card {
    padding: 40px 24px 24px;
    margin: 0 16px;
  }
  
  .app-title {
    font-size: 20px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .features {
    flex-direction: column;
    gap: 12px;
  }
  
  .feature-item {
    flex-direction: row;
    justify-content: center;
    text-align: left;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .login-container {
    background: #1c1c1e;
  }
  
  .login-card {
    background: #2c2c2e;
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 2px 16px rgba(0, 0, 0, 0.3);
  }
  
  .app-title,
  .login-title {
    color: #f2f2f7;
  }
  
  .app-subtitle,
  .login-description {
    color: #8e8e93;
  }
  
  .feature-item span {
    color: #f2f2f7;
  }
  
  .footer-text {
    color: #8e8e93;
  }
  
  .feature-item {
    background: rgba(255, 255, 255, 0.05);
  }
  
  .feature-item:hover {
    background: rgba(255, 255, 255, 0.1);
  }
  
  .footer {
    border-top: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .error-message {
    background: rgba(255, 69, 58, 0.15);
    border: 1px solid rgba(255, 69, 58, 0.3);
    color: #ff453a;
  }
}
</style> 