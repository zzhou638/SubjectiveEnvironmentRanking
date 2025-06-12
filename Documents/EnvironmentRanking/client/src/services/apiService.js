// API服务文件
// 统一封装所有的HTTP请求

import { API_URLS } from '@/config/api'

// 基础fetch配置
const defaultOptions = {
  credentials: 'include', // 包含cookies
  headers: {
    'Content-Type': 'application/json'
  }
}

// HTTP请求封装
class ApiService {
  // GET请求
  static async get(url, options = {}) {
    const config = {
      ...defaultOptions,
      method: 'GET',
      ...options
    }
    
    try {
      const response = await fetch(url, config)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      return await response.json()
    } catch (error) {
      console.error('GET request failed:', error)
      throw error
    }
  }

  // POST请求
  static async post(url, data = null, options = {}) {
    const config = {
      ...defaultOptions,
      method: 'POST',
      ...options
    }
    
    if (data) {
      config.body = JSON.stringify(data)
    }
    
    try {
      const response = await fetch(url, config)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      return await response.json()
    } catch (error) {
      console.error('POST request failed:', error)
      throw error
    }
  }

  // 用户登录
  static async login(loginData = {}) {
    return this.post(API_URLS.AUTH.LOGIN, loginData)
  }

  // 获取图片对
  static async getImagePair() {
    return this.get(API_URLS.RANKING.GET_PAIR)
  }

  // 提交选择
  static async submitChoice(choiceData) {
    return this.post(API_URLS.RANKING.SUBMIT_CHOICE, choiceData)
  }
}

export default ApiService 