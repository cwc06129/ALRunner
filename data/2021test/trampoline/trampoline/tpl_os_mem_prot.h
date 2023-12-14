struct TPL_MEM_REGION {
  void * start;
  void * end;
};
typedef struct TPL_MEM_REGION tpl_mem_region;
struct TPL_MEM_PROT_DESC {
    tpl_mem_region proc_var;
    tpl_mem_region proc_stack;
};
typedef struct TPL_MEM_PROT_DESC tpl_mem_prot_desc;
