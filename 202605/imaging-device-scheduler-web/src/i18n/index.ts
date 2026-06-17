import { createI18n } from 'vue-i18n'

const i18n = createI18n({
  legacy: false,
  locale: 'zh-CN',
  messages: {
    'zh-CN': {
      common: {
        add: '新增',
        edit: '编辑',
        delete: '删除',
        search: '搜索',
        reset: '重置',
        confirm: '确认',
        cancel: '取消',
        save: '保存',
        status: '状态',
        operation: '操作',
        enable: '启用',
        disable: '禁用',
      },
    },
  },
})

export default i18n
