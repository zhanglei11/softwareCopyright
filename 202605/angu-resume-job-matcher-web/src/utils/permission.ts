import type { MenuNode } from '@/types'

export function extractPermissions(menus: MenuNode[]) {
  const permissions = new Set<string>()
  const walk = (nodes: MenuNode[]) => {
    nodes.forEach((node) => {
      if (node.permCode) permissions.add(node.permCode)
      if (node.children?.length) walk(node.children)
    })
  }
  walk(menus)
  return Array.from(permissions)
}

export function extractMenuPaths(menus: MenuNode[]) {
  const paths = new Set<string>()
  const walk = (nodes: MenuNode[]) => {
    nodes.forEach((node) => {
      if (node.menuType === 1 && node.path) paths.add(node.path)
      if (node.children?.length) walk(node.children)
    })
  }
  walk(menus)
  return Array.from(paths)
}

export function hasPermission(target: string | string[] | undefined, permissions: string[]) {
  if (!target) return true
  if (Array.isArray(target)) return target.some((item) => permissions.includes(item))
  return permissions.includes(target)
}

export function collectOpenKeys(tree: MenuNode[], path: string, chain: string[] = []): string[] {
  for (const node of tree) {
    const currentKey = node.path || `dir-${node.id}`
    const nextChain = node.menuType === 0 ? [...chain, currentKey] : chain
    if (node.path === path) return nextChain
    if (node.children?.length) {
      const found = collectOpenKeys(node.children, path, nextChain)
      if (found.length) return found
    }
  }
  return []
}