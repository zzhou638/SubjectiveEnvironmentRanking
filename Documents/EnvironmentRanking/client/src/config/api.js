// API配置文件
// 在这里统一配置所有的API地址，方便部署时修改

// 开发环境和生产环境的API基础URL
const API_CONFIG = {
  // 开发环境
  development: {
    baseURL: 'http://localhost:8081/api'
  },
  // 生产环境 - 部署时修改这里
  production: {
    baseURL: 'https://your-domain.com/api'  // 部署时替换为实际域名
  }
}

// 根据当前环境获取基础URL
const getBaseURL = () => {
  const env = process.env.NODE_ENV || 'development'
  return API_CONFIG[env].baseURL
}

// API端点配置
export const API_ENDPOINTS = {
  // 认证相关
  AUTH: {
    LOGIN: '/auth/login'
  },
  // 排序比较相关
  RANKING: {
    GET_PAIR: '/ranking/get-pair',
    SUBMIT_CHOICE: '/ranking/submit-choice'
  }
}

// 导出基础URL
export const BASE_URL = getBaseURL()

// 导出完整的API URL构建函数
export const buildApiUrl = (endpoint) => {
  return `${BASE_URL}${endpoint}`
}

// 预构建的API URLs
export const API_URLS = {
  AUTH: {
    LOGIN: buildApiUrl(API_ENDPOINTS.AUTH.LOGIN)
  },
  RANKING: {
    GET_PAIR: buildApiUrl(API_ENDPOINTS.RANKING.GET_PAIR),
    SUBMIT_CHOICE: buildApiUrl(API_ENDPOINTS.RANKING.SUBMIT_CHOICE)
  }
} 