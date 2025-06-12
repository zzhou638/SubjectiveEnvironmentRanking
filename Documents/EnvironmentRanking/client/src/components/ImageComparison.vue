<template>
  <div class="comparison-container">
    <div class="comparison-content">
      <!-- 全局加载状态 -->
      <div v-if="isLoading" class="global-loading">
        <div class="loading-spinner"></div>
        <p class="loading-text">{{ texts.loadingImages }}</p>
      </div>

      <!-- 图片比较区域 -->
      <div v-else class="images-section">
        <div class="image-container">
          <!-- 左侧图片 -->
          <div class="image-wrapper left-image">
            <div class="image-label">
              <span class="label-text">{{ texts.leftLabel }}</span>
              <svg class="label-icon" viewBox="0 0 24 24">
                <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"/>
              </svg>
            </div>
            <div class="image-frame">
              <img 
                v-if="leftImage" 
                :src="leftImage" 
                :alt="texts.leftImageAlt"
                class="comparison-image"
                @load="onImageLoad('left')"
                @error="onImageError('left')"
              />
              <div v-else class="image-placeholder">
                <svg class="placeholder-icon" viewBox="0 0 24 24">
                  <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z"/>
                </svg>
                <span>{{ texts.loading }}</span>
              </div>
            </div>
          </div>

          <!-- 分隔符 -->
          <div class="vs-divider">
            <span class="vs-text">VS</span>
          </div>

          <!-- 右侧图片 -->
          <div class="image-wrapper right-image">
            <div class="image-label">
              <svg class="label-icon" viewBox="0 0 24 24">
                <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"/>
              </svg>
              <span class="label-text">{{ texts.rightLabel }}</span>
            </div>
            <div class="image-frame">
              <img 
                v-if="rightImage" 
                :src="rightImage" 
                :alt="texts.rightImageAlt"
                class="comparison-image"
                @load="onImageLoad('right')"
                @error="onImageError('right')"
              />
              <div v-else class="image-placeholder">
                <svg class="placeholder-icon" viewBox="0 0 24 24">
                  <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z"/>
                </svg>
                <span>{{ texts.loading }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 提问和选择区域 -->
      <div v-if="!isLoading" class="question-section">
        <h2 class="question-text">
          {{ texts.questionText }}
        </h2>
        
        <div class="choice-buttons">
          <button 
            class="choice-btn left-choice"
            @click="makeChoice('left')"
            :disabled="isSubmitting"
          >
            <svg class="choice-icon" viewBox="0 0 24 24">
              <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"/>
            </svg>
            <span>{{ texts.leftChoice }}</span>
          </button>
          
          <button 
            class="choice-btn right-choice"
            @click="makeChoice('right')"
            :disabled="isSubmitting"
          >
            <span>{{ texts.rightChoice }}</span>
            <svg class="choice-icon" viewBox="0 0 24 24">
              <path d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"/>
            </svg>
          </button>
        </div>

        <!-- 提交状态 -->
        <div v-if="isSubmitting" class="submitting-status">
          <div class="spinner"></div>
          <span>{{ texts.submitting }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ApiService from '@/services/apiService'

export default {
  name: 'ImageComparison',
  props: {
    language: {
      type: String,
      default: 'zh'
    }
  },
  data() {
    return {
      leftImage: null,
      rightImage: null,
      leftImageId: null,
      rightImageId: null,
      isSubmitting: false,
      isLoading: true,
      imagesLoaded: {
        left: false,
        right: false
      }
    }
  },
  computed: {
    texts() {
      const translations = {
        zh: {
          leftLabel: '左边',
          rightLabel: '右边',
          leftImageAlt: '左侧环境图片',
          rightImageAlt: '右侧环境图片',
          loading: '加载中...',
          questionText: '网页中所示的2张图片您认为哪一张的环境更优美，更适合居住？',
          leftChoice: '左边',
          rightChoice: '右边',
          submitting: '正在提交您的选择...',
          loadingImages: '图片加载中...'
        },
        en: {
          leftLabel: 'Left',
          rightLabel: 'Right',
          leftImageAlt: 'Left environment image',
          rightImageAlt: 'Right environment image',
          loading: 'Loading...',
          questionText: 'Which of the 2 images shown on the webpage do you think has a more beautiful environment and is more suitable for living?',
          leftChoice: 'Left',
          rightChoice: 'Right',
          submitting: 'Submitting your choice...',
          loadingImages: 'Loading images...'
        }
      };
      return translations[this.language];
    }
  },
  watch: {
    language(newLanguage, oldLanguage) {
      console.log(`ImageComparison语言切换: ${oldLanguage} -> ${newLanguage}`);
      console.log('当前texts对象:', this.texts);
    }
  },
  mounted() {
    console.log('ImageComparison组件挂载，当前语言:', this.language);
    console.log('当前texts对象:', this.texts);
    this.loadImages();
  },
  methods: {
    async loadImages() {
      try {
        this.isLoading = true;
        console.log(this.language === 'zh' ? '开始加载图片...' : 'Starting to load images...');
        
        // 使用统一的API服务
        const data = await ApiService.getImagePair();

        console.log('=== API返回数据调试 ===');
        console.log('完整数据:', data);
        console.log('数据类型:', typeof data);
        console.log('数据键值:', Object.keys(data));
        
        // 处理数据结构
        if (!data.left && !data.right) {
          console.log('扁平数据结构处理');
          if (data.left_image_id && data.right_image_id) {
            this.leftImageId = data.left_image_id;
            this.rightImageId = data.right_image_id;
            console.log('图片ID - 左:', this.leftImageId, '右:', this.rightImageId);
            
            this.leftImage = await this.processImageData(data.left_image, 'left');
            this.rightImage = await this.processImageData(data.right_image, 'right');
          } else {
            throw new Error('API返回数据格式无法识别，缺少图片ID字段');
          }
        } else {
          console.log('嵌套数据结构处理');
          if (data.left) {
            this.leftImageId = data.left.left_image_id;
            this.leftImage = await this.processImageData(data.left.left_image, 'left');
          }
          if (data.right) {
            this.rightImageId = data.right.right_image_id;
            this.rightImage = await this.processImageData(data.right.right_image, 'right');
          }
        }

        console.log('=== 最终图片结果 ===');
        console.log('左侧图片URL类型:', typeof this.leftImage);
        console.log('右侧图片URL类型:', typeof this.rightImage);
        console.log('左侧图片:', this.leftImage ? this.leftImage.substring(0, 100) + '...' : 'null');
        console.log('右侧图片:', this.rightImage ? this.rightImage.substring(0, 100) + '...' : 'null');
        
        // 验证图片是否有效
        if (!this.leftImage || !this.rightImage) {
          console.warn('部分图片为空，使用占位图片');
          this.loadPlaceholderImages();
        }
        
      } catch (error) {
        console.error('加载图片失败:', error);
        this.showError(error.message);
        this.loadPlaceholderImages();
      } finally {
        this.isLoading = false;
      }
    },
    
    async processImageData(imageData, side) {
      console.log(`=== 处理${side}侧图片 ===`);
      console.log('数据类型:', typeof imageData);
      console.log('数据内容预览:', imageData ? (typeof imageData === 'string' ? imageData.substring(0, 50) + '...' : imageData) : 'null');
      
      if (!imageData) {
        console.warn(`${side}侧图片数据为空`);
        return null;
      }
      
      try {
        if (typeof imageData === 'string') {
          if (imageData.startsWith('data:')) {
            console.log(`${side}: 已是data URL格式`);
            return imageData;
          } else {
            console.log(`${side}: 转换为data URL`);
            
            // 检测base64字符串特征，判断可能的图片格式
            let mimeType = 'image/jpeg'; // 默认JPEG
            
            // TIFF的魔术数字检测
            if (imageData.length > 0) {
              const decoded = atob(imageData.substring(0, 8));
              const bytes = new Uint8Array(decoded.length);
              for (let i = 0; i < decoded.length; i++) {
                bytes[i] = decoded.charCodeAt(i);
              }
              
              // TIFF文件头检测
              if ((bytes[0] === 0x4D && bytes[1] === 0x4D) || 
                  (bytes[0] === 0x49 && bytes[1] === 0x49)) {
                mimeType = 'image/tiff';
                console.log(`${side}: 检测到TIFF格式`);
              }
              // JPEG文件头检测
              else if (bytes[0] === 0xFF && bytes[1] === 0xD8) {
                mimeType = 'image/jpeg';
                console.log(`${side}: 检测到JPEG格式`);
              }
              // PNG文件头检测
              else if (bytes[0] === 0x89 && bytes[1] === 0x50) {
                mimeType = 'image/png';
                console.log(`${side}: 检测到PNG格式`);
              }
            }
            
            const dataUrl = `data:${mimeType};base64,${imageData}`;
            
            // 如果是TIFF格式，尝试创建一个canvas转换
            if (mimeType === 'image/tiff') {
              console.log(`${side}: TIFF格式，尝试转换`);
              return await this.convertTiffImage(dataUrl, side);
            }
            
            return dataUrl;
          }
        } else if (imageData instanceof ArrayBuffer || imageData instanceof Uint8Array) {
          console.log(`${side}: 二进制数据转Blob`);
          
          // 检测文件头来确定图片格式
          const bytes = new Uint8Array(imageData);
          let mimeType = 'image/jpeg';
          
          if (bytes.length >= 2) {
            if ((bytes[0] === 0x4D && bytes[1] === 0x4D) || 
                (bytes[0] === 0x49 && bytes[1] === 0x49)) {
              mimeType = 'image/tiff';
            } else if (bytes[0] === 0xFF && bytes[1] === 0xD8) {
              mimeType = 'image/jpeg';
            } else if (bytes[0] === 0x89 && bytes[1] === 0x50) {
              mimeType = 'image/png';
            }
          }
          
          const blob = new Blob([imageData], { type: mimeType });
          const url = URL.createObjectURL(blob);
          
          console.log(`${side}: 创建Blob URL成功，格式: ${mimeType}`);
          return url;
        } else if (imageData instanceof Blob) {
          console.log(`${side}: Blob转URL`);
          return URL.createObjectURL(imageData);
        } else if (typeof imageData === 'object') {
          console.log(`${side}: 对象格式，查找数据字段`);
          const keys = Object.keys(imageData);
          console.log('对象键值:', keys);
          
          for (const key of ['data', 'buffer', 'content', 'image', 'base64']) {
            if (imageData[key]) {
              console.log(`在${key}字段找到数据`);
              return await this.processImageData(imageData[key], side);
            }
          }
          
          return null;
        } else {
          console.warn(`${side}: 未知数据格式`);
          return null;
        }
      } catch (error) {
        console.error(`处理${side}侧图片出错:`, error);
        return null;
      }
    },
    
    async convertTiffImage(dataUrl, side) {
      return new Promise((resolve) => {
        console.log(`${side}: 尝试TIFF图片转换`);
        
        const img = new Image();
        img.onload = () => {
          try {
            console.log(`${side}: TIFF图片加载成功，尺寸: ${img.width}x${img.height}`);
            
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            canvas.width = img.width;
            canvas.height = img.height;
            
            ctx.drawImage(img, 0, 0);
            const convertedDataUrl = canvas.toDataURL('image/jpeg', 0.9);
            
            console.log(`${side}: TIFF转JPEG成功`);
            resolve(convertedDataUrl);
          } catch (error) {
            console.error(`${side}: TIFF转换失败:`, error);
            resolve(dataUrl); // 返回原始URL
          }
        };
        
        img.onerror = (error) => {
          console.error(`${side}: TIFF图片加载失败:`, error);
          console.log(`${side}: 回退到原始data URL`);
          resolve(dataUrl);
        };
        
        img.src = dataUrl;
        
        // 设置超时
        setTimeout(() => {
          console.warn(`${side}: TIFF转换超时，使用原始URL`);
          resolve(dataUrl);
        }, 5000);
      });
    },
    
    loadPlaceholderImages() {
      console.log('使用占位图片');
      this.leftImageId = 'placeholder-left';
      this.rightImageId = 'placeholder-right';
      this.leftImage = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjMwMCIgZmlsbD0iIzAwN0FGRiIvPjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMjQiIGZpbGw9IndoaXRlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBkeT0iLjNlbSI+左侧图片</dGV4dD48L3N2Zz4=';
      this.rightImage = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMzAwIiBoZWlnaHQ9IjMwMCIgZmlsbD0iIzVBNTZENiIvPjx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMjQiIGZpbGw9IndoaXRlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBkeT0iLjNlbSI+右侧图片</dGV4dD48L3N2Zz4=';
    },
    
    showError(message) {
      console.error('错误信息:', message);
      // TODO: 可以添加用户界面错误提示
    },
    
    onImageLoad(side) {
      this.imagesLoaded[side] = true;
      console.log(`${side}侧图片加载成功`);
    },
    
    onImageError(side) {
      console.error(`${side}侧图片加载失败`);
      // 添加图片加载失败的处理
      if (side === 'left') {
        this.leftImage = this.createErrorPlaceholder('左侧图片加载失败');
      } else {
        this.rightImage = this.createErrorPlaceholder('右侧图片加载失败');
      }
    },
    
    createErrorPlaceholder(text) {
      return `data:image/svg+xml;base64,${btoa(`
        <svg width="300" height="300" xmlns="http://www.w3.org/2000/svg">
          <rect width="300" height="300" fill="#ff6b6b"/>
          <text x="50%" y="50%" font-family="Arial" font-size="16" fill="white" text-anchor="middle" dy=".3em">${text}</text>
        </svg>
      `)}`;
    },
    
    async makeChoice(choice) {
      this.isSubmitting = true;
      
      try {
        const choiceText = choice === 'left' ? this.texts.leftChoice : this.texts.rightChoice;
        console.log(`${this.language === 'zh' ? '用户选择:' : 'User choice:'} ${choiceText}`);
        
        // 验证必要的数据
        if (!this.leftImageId || !this.rightImageId) {
          throw new Error('图片ID缺失，无法提交选择');
        }
        
        // 准备提交数据 - 按照后端API要求的格式
        const submitData = {
          left_image_id: this.leftImageId,
          right_image_id: this.rightImageId,
          choice: choice  // "left" 或 "right"
        };
        
        console.log('=== 提交选择数据 ===');
        console.log('提交的数据:', submitData);
        console.log('API端点: http://localhost:8081/api/ranking/submit-choice');
        
        // 使用统一的API服务提交用户选择
        const result = await ApiService.submitChoice(submitData);
        
        console.log('=== 提交成功 ===');
        console.log('服务器响应:', result);
        
        // 显示成功消息
        if (result.message) {
          console.log('提交结果:', result.message);
        }
        
        // 触发父组件事件（如果需要）
        this.$emit('choice-made', {
          choice: choice,
          leftImageId: this.leftImageId,
          rightImageId: this.rightImageId,
          result: result
        });
        
        // 提交成功后，自动加载下一对图片
        console.log('加载下一对图片...');
        await this.loadImages();
        
      } catch (error) {
        console.error('=== 提交选择失败 ===');
        console.error('错误信息:', error.message);
        console.error('完整错误:', error);
        
        // 显示用户友好的错误信息
        this.showError(`提交选择失败: ${error.message}`);
        
        // 可以在这里添加用户界面的错误提示
        alert(`提交选择失败: ${error.message}`);
        
      } finally {
        this.isSubmitting = false;
      }
    },
    
    // 组件销毁时清理blob URLs
    beforeDestroy() {
      if (this.leftImage && this.leftImage.startsWith('blob:')) {
        URL.revokeObjectURL(this.leftImage);
      }
      if (this.rightImage && this.rightImage.startsWith('blob:')) {
        URL.revokeObjectURL(this.rightImage);
      }
    }
  }
}
</script>

<style scoped>
.comparison-container {
  min-height: 100vh;
  background: #ffffff;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
  padding: 20px;
}

.comparison-content {
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 0;
}

.global-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: 20px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(0, 122, 255, 0.2);
  border-top: 3px solid #007AFF;
  border-radius: 50%;
  animation: globalSpin 1s linear infinite;
}

.loading-text {
  font-size: 18px;
  color: #86868b;
  font-weight: 500;
  margin: 0;
}

@keyframes globalSpin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.images-section {
  margin-bottom: 60px;
}

.image-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 40px;
  flex-wrap: wrap;
}

.image-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.image-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #1d1d1f;
}

.label-icon {
  width: 20px;
  height: 20px;
  fill: #007AFF;
}

.image-frame {
  width: 300px;
  height: 300px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.image-frame:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.comparison-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f5f5f7;
  color: #86868b;
  gap: 12px;
}

.placeholder-icon {
  width: 48px;
  height: 48px;
  fill: currentColor;
  opacity: 0.6;
}

.vs-divider {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60px;
}

.vs-text {
  font-size: 24px;
  font-weight: 700;
  color: #86868b;
  background: white;
  padding: 12px 20px;
  border-radius: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.question-section {
  text-align: center;
}

.question-text {
  font-size: 24px;
  font-weight: 600;
  color: #1d1d1f;
  line-height: 1.4;
  margin: 0 0 40px 0;
  max-width: 90%;
  margin-left: auto;
  margin-right: auto;
}

.choice-buttons {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 30px;
}

.choice-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 32px;
  background: #007AFF;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px rgba(0, 122, 255, 0.3);
  min-width: 120px;
  justify-content: center;
}

.choice-btn:hover:not(:disabled) {
  background: #0056CC;
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 122, 255, 0.4);
}

.choice-btn:active:not(:disabled) {
  transform: translateY(0);
  background: #004499;
}

.choice-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.choice-icon {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

.submitting-status {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: #86868b;
  font-size: 16px;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(134, 134, 139, 0.3);
  border-top: 2px solid #86868b;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .comparison-content {
    padding: 20px 0;
  }
  
  .image-container {
    flex-direction: column;
    gap: 30px;
  }
  
  .image-frame {
    width: 280px;
    height: 280px;
  }
  
  .vs-divider {
    order: 1;
    min-height: 40px;
  }
  
  .left-image {
    order: 0;
  }
  
  .right-image {
    order: 2;
  }
  
  .question-text {
    font-size: 20px;
  }
  
  .choice-buttons {
    flex-direction: column;
    align-items: center;
    gap: 16px;
  }
  
  .choice-btn {
    width: 200px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .comparison-container {
    background: #1c1c1e;
  }
  
  .page-title,
  .question-text {
    color: #f2f2f7;
  }
  
  .page-subtitle {
    color: #8e8e93;
  }
  
  .image-label {
    color: #f2f2f7;
  }
  
  .image-frame {
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  }
  
  .image-placeholder {
    background: #2c2c2e;
    color: #8e8e93;
  }
  
  .vs-text {
    background: #2c2c2e;
    color: #8e8e93;
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
  }
  
  .submitting-status {
    color: #8e8e93;
  }
  
  .loading-text {
    color: #8e8e93;
  }
  
  .loading-spinner {
    border: 3px solid rgba(0, 122, 255, 0.2);
    border-top: 3px solid #007AFF;
  }
}
</style> 