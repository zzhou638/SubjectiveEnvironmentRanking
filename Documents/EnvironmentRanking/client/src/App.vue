<template>
  <div id="app">
    <LoginPage 
      v-if="!isLoggedIn" 
      @login-success="handleLoginSuccess"
      :language="currentLanguage"
    />
    <div v-else class="main-app">
      <div class="header">
        <h1>{{ texts.appTitle }}</h1>
        <div class="header-actions">
          <button @click="toggleLanguage" class="language-btn">
            <svg class="language-icon" viewBox="0 0 24 24">
              <path d="M12.87 15.07l-2.54-2.51.03-.03A17.52 17.52 0 0014.07 6H17V4h-7V2H8v2H1v2h11.17C11.5 7.92 10.44 9.75 9 11.35 8.07 10.32 7.3 9.19 6.69 8h-2c.73 1.63 1.73 3.17 2.98 4.56l-5.09 5.02L4 19l5-5 3.11 3.11.76-2.04zM18.5 10h-2L12 22h2l1.12-3h4.75L21 22h2l-4.5-12zm-2.62 7l1.62-4.33L19.12 17h-3.24z"/>
            </svg>
            {{ currentLanguage === 'zh' ? 'EN' : '中' }}
          </button>
          <button @click="handleLogout" class="logout-btn">{{ texts.logout }}</button>
        </div>
      </div>
      <ImageComparison 
        :key="currentLanguage"
        @choice-made="handleChoiceMade"
        :language="currentLanguage"
      />
    </div>
  </div>
</template>

<script>
import LoginPage from './components/LoginPage.vue'
import ImageComparison from './components/ImageComparison.vue'

export default {
  name: 'App',
  components: {
    LoginPage,
    ImageComparison
  },
  data() {
    return {
      isLoggedIn: false,
      currentLanguage: 'zh' // 'zh' for Chinese, 'en' for English
    }
  },
  computed: {
    texts() {
      const translations = {
        zh: {
          appTitle: '城中村环境主观评价系统',
          logout: '退出登录'
        },
        en: {
          appTitle: 'Urban Village Environment Assessment System',
          logout: 'Logout'
        }
      };
      return translations[this.currentLanguage];
    }
  },
  methods: {
    handleLoginSuccess() {
      this.isLoggedIn = true;
      console.log('用户登录成功，进入图片比较页面');
    },
    handleLogout() {
      this.isLoggedIn = false;
      console.log('用户已退出登录');
    },
    handleChoiceMade(choice) {
      console.log('用户做出选择:', choice === 'left' ? '左边' : '右边');
      // 这里可以添加选择统计或其他逻辑
    },
    toggleLanguage() {
      const oldLanguage = this.currentLanguage;
      this.currentLanguage = this.currentLanguage === 'zh' ? 'en' : 'zh';
      console.log('App语言切换:', oldLanguage, '->', this.currentLanguage);
      console.log('当前App texts:', this.texts);
      console.log('语言已切换为:', this.currentLanguage === 'zh' ? '中文' : 'English');
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
}

.main-app {
  min-height: 100vh;
  background: #ffffff;
}

.header {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  padding: 16px 40px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.header h1 {
  color: #1d1d1f;
  font-size: 20px;
  font-weight: 600;
  letter-spacing: -0.3px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.language-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: #f5f5f7;
  color: #1d1d1f;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.language-btn:hover {
  background: #e5e5e7;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.language-btn:active {
  transform: translateY(0);
  background: #d1d1d6;
}

.language-icon {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

.logout-btn {
  padding: 8px 16px;
  background: #007AFF;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 122, 255, 0.3);
}

.logout-btn:hover {
  background: #0056CC;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 122, 255, 0.4);
}

.logout-btn:active {
  transform: translateY(0);
  background: #004499;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 12px 20px;
    flex-direction: column;
    gap: 12px;
    position: relative;
  }
  
  .header h1 {
    font-size: 18px;
    text-align: center;
  }
  
  .header-actions {
    gap: 8px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .main-app {
    background: #1c1c1e;
  }
  
  .header {
    background: rgba(28, 28, 30, 0.8);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  }
  
  .header h1 {
    color: #f2f2f7;
  }
  
  .language-btn {
    background: #2c2c2e;
    color: #f2f2f7;
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .language-btn:hover {
    background: #3a3a3c;
  }
  
  .language-btn:active {
    background: #48484a;
  }
}
</style>
