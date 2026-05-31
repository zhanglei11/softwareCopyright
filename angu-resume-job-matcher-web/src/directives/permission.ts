import type { Directive } from 'vue'
import { pinia, useAppStore } from '@/store'
import { hasPermission } from '@/utils/permission'

function applyPermission(el: HTMLElement, binding: { value?: string | string[] }) {
  const store = useAppStore(pinia)
  if (!hasPermission(binding.value, store.permissions)) {
    el.parentNode?.removeChild(el)
  }
}

const permissionDirective: Directive = {
  mounted(el, binding) {
    applyPermission(el as HTMLElement, binding)
  },
  updated(el, binding) {
    applyPermission(el as HTMLElement, binding)
  },
}

export default permissionDirective