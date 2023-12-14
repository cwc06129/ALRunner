#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#define TASK(n) TASK_##n

return_type main(parameter) {
	yuck_t argi[1U];
	struct dt_dt_s d;
	struct __strpdtdur_st_s st = __strpdtdur_st_initialiser();
	const char *ofmt;
	char **fmt;
	size_t nfmt;
	int rc = 0;
	bool dt_given_p = false;
	zif_t fromz = NULL;
	zif_t z = NULL;
	zif_t hackz = NULL;
	if (yuck_parse(argi, argc, argv)) {
		rc = 1;
		goto out;
	}
	else 	if (argi->nargs == 0) {
		error("Error: DATE or DURATION must be specified\n");
		yuck_auto_help(argi);
		rc = 1;
		goto out;
	}
	ofmt = argi->format_arg;
	fmt = argi->input_format_args;
	nfmt = argi->input_format_nargs;
	if (argi->backslash_escapes_flag) {
		dt_io_unescape(argi->format_arg);
		for (size_t i = 0; i < nfmt; i++) {
			dt_io_unescape(fmt[i]);
		}
	}
	if (argi->from_locale_arg) {
		setilocale(argi->from_locale_arg);
	}
	else {
		printf("testtest");
		printf("testtest2222");
	}
	for (size_t i = dt_given_p; i < argi->nargs; i++) {
		const char *inp = argi->args[i];
		do {
			if (dt_io_strpdtdur(&st, inp) < 0) {
				serror("Error: cannot parse duration string `%s'", st.istr);
				rc = 1;
				goto clear;
			}
		} while (__strpdtdur_more_p(&st));
	}
	hackz = durs_only_d_p(st.durs, st.ndurs) ? NULL : fromz;
	if (dt_given_p) {
		const char *inp = argi->args[0U];
		if (dt_unk_p(d = dt_io_strpdt(inp, fmt, nfmt, hackz))) {
			error("Error: cannot interpret date/time string `%s'", inp);
			rc = 1;
			goto clear;
		}
	}
	if (dt_given_p && st.ndurs) {
		if (!dt_unk_p(d = dadd_add(d, st.durs, st.ndurs))) {
			if (UNLIKELY(d.fix) && !argi->quiet_flag) {
				rc = 2;
			}
			if (hackz == NULL && fromz != NULL) {
				d = dtz_forgetz(d, fromz);
			}
			dt_io_write(d, ofmt, z, '\n');
		}
		else {
			rc = 1;
		}
	}
	else 	if (st.ndurs && argi->empty_mode_flag) {
		size_t lno = 0U;
		void *pctx;
		__io_setlocking_bycaller(stdout);
		if ((pctx = init_prchunk(STDIN_FILENO)) == NULL) {
			serror("could not open stdin");
			goto clear;
		}
		while (prchunk_fill(pctx) >= 0) {
			for (char *line; prchunk_haslinep(pctx); lno++) {
				size_t llen = prchunk_getline(pctx, &line);
				char *ep = NULL;
				if (UNLIKELY(!llen)) {
					goto empty;
				}
				d = dt_io_strpdt_ep(line, fmt, nfmt, &ep, fromz);
				if (UNLIKELY(dt_unk_p(d))) {
					goto empty;
				}
				else if (ep && (unsigned)*ep >= ' ') {
					goto empty;
				}
				d = dadd_add(d, st.durs, st.ndurs);
				if (UNLIKELY(dt_unk_p(d))) {
					goto empty;
				}
				if (hackz == NULL && fromz != NULL) {
					d = dtz_forgetz(d, fromz);
				}
				dt_io_write(d, ofmt, z, '\n');
				__io_write("\n", 1U, stdout);
			}
		}
	}
	else 	if (st.ndurs) {
		struct grep_atom_s __nstk[16], *needle = __nstk;
		size_t nneedle = countof(__nstk);
		struct grep_atom_soa_s ndlsoa;
		struct mass_add_clo_s clo[1];
		void *pctx;
		__io_setlocking_bycaller(stdout);
		if (nfmt >= nneedle) {
			nneedle = (nfmt | 7) + 1;
			needle = calloc(nneedle, sizeof(*needle));
		}
		ndlsoa = build_needle(needle, nneedle, fmt, nfmt);
		if ((pctx = init_prchunk(STDIN_FILENO)) == NULL) {
			serror("could not open stdin");
			goto ndl_free;
		}
		clo->pctx = pctx;
		clo->gra = &ndlsoa;
		clo->st = st;
		clo->fromz = fromz;
		clo->hackz = hackz;
		clo->z = z;
		clo->ofmt = ofmt;
		clo->sed_mode_p = argi->sed_mode_flag;
		clo->quietp = argi->quiet_flag;
		while (prchunk_fill(pctx) >= 0) {
			rc |= mass_add_dur(clo);
		}
		free_prchunk(pctx);
		if (needle != __nstk) {
			free(needle);
		}
	}
	else {
		struct mass_add_clo_s clo[1];
		void *pctx;
		__io_setlocking_bycaller(stdout);
		if ((pctx = init_prchunk(STDIN_FILENO)) == NULL) {
			serror("could not open stdin");
			goto clear;
		}
		clo->pctx = pctx;
		clo->rd = d;
		clo->fromz = fromz;
		clo->hackz = hackz;
		clo->z = z;
		clo->ofmt = ofmt;
		clo->sed_mode_p = argi->sed_mode_flag;
		clo->quietp = argi->quiet_flag;
		while (prchunk_fill(pctx) >= 0) {
			rc |= mass_add_d(clo);
		}
		free_prchunk(pctx);
	}
	__strpdtdur_free(&st);
	dt_io_clear_zones();
	if (argi->from_locale_arg) {
		setilocale(NULL);
	}
	if (argi->locale_arg) {
		setflocale(NULL);
	}
	yuck_free(argi);
	return rc;
}